package com.coder.notificationsampleapp.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_SECRET
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import com.coder.data.message.Message
import com.coder.notificationsampleapp.MainActivity2
import com.coder.notificationsampleapp.broadcast_receiver.MyBroadCastReceiver
import com.coder.notificationsampleapp.notifcation_channels.NotificationChannels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.coder.notificationsampleapp.R


object Notification {
    const val KEY_TEXT_REPLY = "key_text_reply"
    const val GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL"
    const val SUMMARY_ID = 0

    fun simpleNotification(context: Context, textTitle: String, textContent: String) {
        val notificationId = 12313
        val builder = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setSmallIcon(com.google.accompanist.permissions.R.drawable.notification_bg)
            .setContentTitle(textTitle)
            .setContentIntent(addContentIntent(context))
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_SECRET)
            .addAction(
                R.drawable.ic_launcher_background,
                "Click",
                addAction(context, "click_action")
            )
            .addAction(replyAction(context))
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManagerCompat.notify(notificationId, builder.build())
    }

    private fun addContentIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity2::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun addAction(context: Context, action: String): PendingIntent {
        val intent = Intent(context, MyBroadCastReceiver::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun replyAction(context: Context): NotificationCompat.Action {
        val replyIntent = Intent(context, MyBroadCastReceiver::class.java)
        replyIntent.action = "reply_action"

        val replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                context,
                1234,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

        return NotificationCompat.Action.Builder(
            androidx.core.R.drawable.notification_action_background,
            "Reply", replyPendingIntent
        )
            .addRemoteInput(createRemoteInput())
            .build()
    }

    private fun createRemoteInput(): RemoteInput {
        return RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel("Add Reply")
            build()
        }
    }

    fun createProgressNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1).apply {
            setContentTitle("Game Download")
            setContentText("Download in progress")
            setSmallIcon(com.google.accompanist.permissions.R.drawable.notification_bg)
            priority = NotificationCompat.PRIORITY_LOW
        }

        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
        notificationManagerCompat.notify(1234, builder.build())

        CoroutineScope(Dispatchers.Default).launch {
            for (i in PROGRESS_CURRENT..PROGRESS_MAX step 10) {
                builder.setProgress(PROGRESS_MAX, i, false)
                notificationManagerCompat.notify(1234, builder.build());
                delay(1000)
            }
            delay(10000)
            builder.setContentText("Download complete")
                .setProgress(0, 0, false)
            notificationManagerCompat.notify(1234, builder.build())
        }
    }

    fun createNotificationWithBackStack(context: Context) {
        val resultIntent = Intent(context, MainActivity2::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setSmallIcon(com.google.accompanist.permissions.R.drawable.notification_bg)
            .setContentTitle("textTitle")
            .setContentIntent(resultPendingIntent)
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_SECRET)
            .addAction(
                R.drawable.ic_launcher_background,
                "Click",
                addAction(context, "click_action")
            )
            .addAction(replyAction(context))
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManagerCompat.notify(12345, builder.build())
    }

    fun createNotificationWithSpecialActivity(context: Context) {
        val notifyIntent = Intent(context, MainActivity2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setSmallIcon(com.google.accompanist.permissions.R.drawable.notification_bg)
            .setContentTitle("textTitle")
            .setContentIntent(notifyPendingIntent)
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_SECRET)
            .addAction(
                R.drawable.ic_launcher_background,
                "Click",
                addAction(context, "click_action")
            )
            .addAction(replyAction(context))
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(123456, builder.build())
        }
    }

    fun createNotificationWithLargeIcon(context: Context) {
        val icon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.aquarium
        )
        val notification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setSmallIcon(R.drawable.desk)
            .setContentTitle("imageTitle")
            .setContentText("imageDescription")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon).bigLargeIcon(null)
            )
            .setLargeIcon(icon)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1234567, notification)
        }
    }

    fun createNotificationWithLargeText(context: Context) {
        val icon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.aquarium
        )

        val notification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setSmallIcon(R.drawable.desk)
            .setContentTitle("Joe")
            .setContentText("Lending Money")
            .setLargeIcon(icon)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("long long long long long long long long long long text ")
            )
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(12544567, notification)
        }
    }

    fun createNotificationWithConversationText(context: Context) {
        val message1 = NotificationCompat.MessagingStyle.Message(
            "Hi, there",
            12345L,
            "Martin"
        )
        val message2 = NotificationCompat.MessagingStyle.Message(
            "Hello",
            12345L,
            "You"
        )

        val notification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setSmallIcon(R.drawable.desk)
            .setStyle(
                NotificationCompat.MessagingStyle("Arun")
                    .addMessage(message1)
                    .addMessage(message2)
            )
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(12544567, notification)
        }
    }

    fun createNotificationWithMediaControl(context: Context) {
        val icon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.aquarium
        )

        val mediaSession = MediaSessionCompat(context, "myTag")

        val notification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.desk)
            .addAction(R.drawable.previous, "Previous", null)
            .addAction(R.drawable.pause, "Pause", null)
            .addAction(R.drawable.next, "Next", null)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setContentTitle("music_land")
            .setContentText("Creative Awesome Band")
            .setLargeIcon(icon)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(12544567, notification)
        }
    }

    fun createFullScreenIntentNotification(context: Context) {
        val fullScreenIntent = Intent(context, MainActivity2::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notificationBuilder =
            NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
                .setSmallIcon(R.drawable.desk)
                .setContentTitle("Incoming call")
                .setContentText("+9178458787XXX")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(fullScreenPendingIntent, true)

        val incomingCallNotification = notificationBuilder.build()
        with(NotificationManagerCompat.from(context)) {
            notify(1445457, incomingCallNotification)
        }
    }

    fun createGroupNotification(context: Context) {
        val icon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.aquarium
        )

        val newMessageNotification1 =
            NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
                .setSmallIcon(R.drawable.email)
                .setContentTitle("Arun")
                .setContentText("Ooo")
                .setLargeIcon(icon)
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

        val newMessageNotification2 =
            NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
                .setSmallIcon(R.drawable.email)
                .setContentTitle("Alex")
                .setContentText("Outside")
                .setLargeIcon(icon)
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

        val summaryNotification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setContentTitle("Summary")
            .setContentText("Two new messages")
            .setSmallIcon(R.drawable.email)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Arun Ooo")
                    .addLine("Alex Outside")
                    .setBigContentTitle("2 new messages")
                    .setSummaryText("arun@example.com")
            )
            .setGroup(GROUP_KEY_WORK_EMAIL)
            .setGroupSummary(true)
            .build()


        with(NotificationManagerCompat.from(context)) {
            notify(2454, newMessageNotification1)
            notify(35, newMessageNotification2)
            notify(SUMMARY_ID, summaryNotification)
        }

    }

    fun createCustomNotification(context: Context) {
        val notificationLayout = RemoteViews(context.packageName, R.layout.notification_small)
        val notificationLayoutExpanded =
            RemoteViews(context.packageName, R.layout.notification_small)

        val customNotification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL1)
            .setSmallIcon(R.drawable.notification_icon)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)
            .build()
        with(NotificationManagerCompat.from(context)) {
            notify(1445457, customNotification)
        }
    }

    fun createBubble(context: Context) {
        Log.i("showNotification", "createBubble")
        BubbleNotificationView(context).showNotification(
            Message(
                123,
                "Arun",
                "Hello",
                R.drawable.ic_base_person
            )
        )
    }


}