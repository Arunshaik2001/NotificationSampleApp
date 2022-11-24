package com.coder.notificationsampleapp.notification

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context.SHORTCUT_SERVICE
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.coder.data.message.Message
import com.coder.notificationsampleapp.MainActivity3
import com.coder.notificationsampleapp.R


class BubbleNotificationView(
    private val context: Context,
    private val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager,
    private val shortcutManager: ShortcutManager = context.getSystemService(SHORTCUT_SERVICE) as ShortcutManager

) {

    companion object {
        private const val CHANNEL_NEW_BUBBLE = "bubble_channel"

        private const val REQUEST_CONTENT = 100
        private const val REQUEST_BUBBLE = 200
    }

    init {
        setUpNotificationChannels()
        clearDynamicShortCuts()
    }

    fun showNotification(message: Message) {
        val icon = IconCompat.createWithResource(context, message.image)
        val contentUri = createContentUri(message.sender)


        val builder = getNotificationBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val person = androidx.core.app.Person.Builder()
                .setName(message.sender)
                .setIcon(icon)
                .setImportant(true)
                .build()
            val bubbleData = createBubbleMetadata(contentUri, icon)

            val shortcut = createDynamicShortcut(
                message,
                icon,
                person
            ).toShortcutInfo()
            addDynamicShortcut(shortcut)

            with(builder) {
                bubbleMetadata = bubbleData
                setStyle(NotificationCompat.MessagingStyle(person).addMessage(
                    NotificationCompat.MessagingStyle.Message(
                        message.text,
                        System.currentTimeMillis(),
                        person
                    )
                ))
                setShortcutId(shortcut.id)
                addPerson(person)
            }


        }

        with(builder) {
            setContentTitle(
                context.resources.getString(
                    R.string.message_from,
                    message.sender
                )
            )
            setSmallIcon(R.drawable.ic_stat_notification)
            setCategory(android.app.Notification.CATEGORY_MESSAGE)
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity3::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
            )
            setShowWhen(true)
        }


        notificationManager.notify(message.id, builder.build())
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, CHANNEL_NEW_BUBBLE)
        } else {
            NotificationCompat.Builder(context)
        }
    }

    private fun addDynamicShortcut(shortcut: ShortcutInfo) {
        if (atLeastAndroid11()) {
            shortcutManager.pushDynamicShortcut(shortcut)
        } else {
            shortcutManager.addDynamicShortcuts(listOf(shortcut))
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createBubbleMetadata(
        contentUri: Uri,
        icon: IconCompat
    ): NotificationCompat.BubbleMetadata {
        val bubbleIntent =
            PendingIntent.getActivity(
                context,
                REQUEST_BUBBLE,
                Intent(context, MainActivity3::class.java)
                    .setAction(Intent.ACTION_VIEW)
                    .setData(contentUri),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

        val builder =  NotificationCompat.BubbleMetadata.Builder(bubbleIntent, icon)

        return builder
            .setDesiredHeightResId(R.dimen.bubble_height)
            .setAutoExpandBubble(true)
            .setSuppressNotification(true)
            .build()
    }

    private fun setUpNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_NEW_BUBBLE,
                context.getString(R.string.notification_channel_name_bubble),
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.setShowBadge(true)

            if (atLeastAndroid11()) {
                notificationChannel.setAllowBubbles(true)
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun atLeastAndroid11() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    private fun clearDynamicShortCuts() {
        shortcutManager.removeAllDynamicShortcuts()
    }

    private fun createContentUri(text: String): Uri {
        return "app://com.coder.notificationsample/message/$text".toUri()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createDynamicShortcut(
        message: Message,
        icon: IconCompat,
        person: androidx.core.app.Person
    ): ShortcutInfoCompat {
        return ShortcutInfoCompat.Builder(context, message.id.toString())
            .setLongLived(true)
            .setIntent(
                Intent(context, MainActivity3::class.java)
                    .setAction(Intent.ACTION_VIEW)
                    .setData(createContentUri(message.text))
            )
            .setShortLabel(message.sender)
            .setIcon(icon)
            .setPerson(person)
            .build()
    }
}
