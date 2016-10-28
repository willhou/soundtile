package com.maize.soundtile;

import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by willhou on 10/28/16.
 */

public class Utils {

    public static boolean checkNotificationPolicyAccess(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return  notificationManager.isNotificationPolicyAccessGranted();
    }
}
