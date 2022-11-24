package com.coder.notificationsampleapp.notifcation_channels

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity

object NotificationChannels {

    const val CHANNEL1 = "channel_id_1"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL1,
                "channel1",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "My first notification channel"
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            notificationChannel.setShowBadge(true)

            val notificationManager = context.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            Log.i("NotificationChannel",notificationChannel.lockscreenVisibility.toString())
        }
    }
}