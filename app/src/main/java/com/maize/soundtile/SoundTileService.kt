/*
 * Created by willhou on 10/28/16.
 */

package com.maize.soundtile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.media.AudioManager
import android.service.quicksettings.TileService

class SoundTileService : TileService() {

    override fun onClick() {
        super.onClick()

        if (!Util.checkNotificationPolicyAccess(this)) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivityAndCollapse(intent)
            return
        }

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        var ringerMode = audioManager.ringerMode
        when (ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> ringerMode = AudioManager.RINGER_MODE_VIBRATE
            AudioManager.RINGER_MODE_VIBRATE -> ringerMode = AudioManager.RINGER_MODE_SILENT
            AudioManager.RINGER_MODE_SILENT -> ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
        audioManager.ringerMode = ringerMode
        updateTile(ringerMode)
    }

    override fun onStartListening() {
        super.onStartListening()
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        updateTile(audioManager.ringerMode)

        val filter = IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onStopListening() {
        super.onStopListening()
        unregisterReceiver(receiver)
    }

    private fun updateTile(ringerMode : Int) {
        val tile = qsTile

        if (!Util.checkNotificationPolicyAccess(this)) {
            tile.icon = Icon.createWithResource(this, R.drawable.ic_outline_error_outline_24px)
            tile.label = getString(R.string.app_name)
            return
        }

        when (ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> {
                tile.icon = Icon.createWithResource(this, R.drawable.ic_outline_volume_up_24px)
                tile.label = getString(R.string.state_normal)
            }

            AudioManager.RINGER_MODE_VIBRATE -> {
                tile.icon = Icon.createWithResource(this, R.drawable.ic_outline_vibration_24px)
                tile.label = getString(R.string.state_vibrate)
            }

            AudioManager.RINGER_MODE_SILENT -> {
                tile.icon = Icon.createWithResource(this, R.drawable.ic_outline_volume_off_24px)
                tile.label = getString(R.string.state_silent)
            }
        }

        tile.updateTile()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.RINGER_MODE_CHANGED_ACTION == intent.action) {
                val ringerMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, AudioManager.MODE_NORMAL)
                updateTile(ringerMode)
            }
        }
    }
}