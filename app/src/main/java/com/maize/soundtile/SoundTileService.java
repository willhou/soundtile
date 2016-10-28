package com.maize.soundtile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

/**
 * Created by willhou on 10/28/16.
 */

public class SoundTileService extends TileService {

    @Override
    public void onClick() {
        super.onClick();

        if (!Utils.checkNotificationPolicyAccess(this)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityAndCollapse(intent);
            return;
        }

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                ringerMode = AudioManager.RINGER_MODE_VIBRATE;
                break;

            case AudioManager.RINGER_MODE_VIBRATE:
                ringerMode = AudioManager.RINGER_MODE_SILENT;
                break;

            case AudioManager.RINGER_MODE_SILENT:
                ringerMode = AudioManager.RINGER_MODE_NORMAL;
                break;
        }
        audioManager.setRingerMode(ringerMode);
        updateTile(ringerMode);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        updateTile(ringerMode);

        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        unregisterReceiver(mReceiver);
    }

    private void updateTile(int ringerMode) {
        Tile tile = getQsTile();

        if (!Utils.checkNotificationPolicyAccess(this)) {
            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_alert_error));
            tile.setLabel(getString(R.string.app_name));
            return;
        }

        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_av_volume_up));
                tile.setLabel(getString(R.string.state_normal));
                break;

            case AudioManager.RINGER_MODE_VIBRATE:
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_notification_vibration));
                tile.setLabel(getString(R.string.state_vibrate));
                break;

            case AudioManager.RINGER_MODE_SILENT:
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_av_volume_off));
                tile.setLabel(getString(R.string.state_silent));
                break;
        }

        tile.updateTile();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction())) {
                int ringerMode = intent.getIntExtra(
                        AudioManager.EXTRA_RINGER_MODE, AudioManager.MODE_NORMAL);
                updateTile(ringerMode);
            }
        }
    };
}
