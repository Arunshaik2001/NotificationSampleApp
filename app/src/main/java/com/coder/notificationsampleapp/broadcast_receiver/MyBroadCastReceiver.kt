package com.coder.notificationsampleapp.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.coder.notificationsampleapp.notifcation_channels.NotificationChannels
import com.coder.notificationsampleapp.notification.Notification

class MyBroadCastReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(p0: Context?, p1: Intent?) {
        val actionString = p1?.action
        Log.i("MyBroadCastReceiver",actionString?:"")

        if(actionString == "reply_action"){
            val repliedNotification = android.app.Notification.Builder(p0!!, NotificationChannels.CHANNEL1)
                .setSmallIcon(androidx.core.R.drawable.notification_bg_low_normal)
                .setContentText(getMessageText(p1))
                .build()
            NotificationManagerCompat.from(p0).apply {
                this.notify(12313, repliedNotification)
            }
        }
        else if(actionString == "click_action"){
            NotificationManagerCompat.from(p0!!).apply {
                this.cancel(12313)
            }
        }
    }

    private fun getMessageText(intent: Intent): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(Notification.KEY_TEXT_REPLY)
    }
}