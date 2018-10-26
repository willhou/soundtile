/*
 * Created by willhou on 10/28/16.
 */

package com.maize.soundtile

import android.app.NotificationManager
import android.content.Context

object Util {

    fun checkNotificationPolicyAccess(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.isNotificationPolicyAccessGranted
    }
}