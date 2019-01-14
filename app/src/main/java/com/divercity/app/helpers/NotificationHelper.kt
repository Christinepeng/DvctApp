package com.divercity.app.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.divercity.app.R
import javax.inject.Inject

/**
 * Created by lucas on 14/01/2019.
 */

class NotificationHelper @Inject
constructor(val context : Context) {

    val CHAT_CHANNEL = "chats"
    private var manager: NotificationManager? = null

    init {
        createChannels()
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val chatChannel = NotificationChannel(
                CHAT_CHANNEL,
                context.getString(R.string.noti_channel_chats),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            chatChannel.enableLights(true)
            chatChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(chatChannel)
        }
    }

    fun getNotification1(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHAT_CHANNEL)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(getSmallIcon())
            .setAutoCancel(true)
    }

    fun notify(id: Int, notification: NotificationCompat.Builder) {
        getManager().notify(id, notification.build())
    }

    private fun getSmallIcon(): Int {
        return android.R.drawable.stat_notify_chat
    }

    private fun getManager(): NotificationManager {
        if (manager == null) {
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return manager as NotificationManager
    }
}