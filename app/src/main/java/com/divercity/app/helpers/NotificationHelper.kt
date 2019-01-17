package com.divercity.app.helpers

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.divercity.app.R
import com.divercity.app.features.chat.chat.ChatActivity
import com.facebook.FacebookSdk.getApplicationContext
import javax.inject.Inject


/**
 * Created by lucas on 14/01/2019.
 */

class NotificationHelper @Inject
constructor(val context: Context) {

    val CHAT_CHANNEL = "chats"
    private var manager: NotificationManager? = null

    init {
        createChannels()
    }

    @SuppressLint("WrongConstant")
    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val chatChannel = NotificationChannel(
                    CHAT_CHANNEL,
                    context.getString(R.string.noti_channel_chats),
                    NotificationManager.IMPORTANCE_MAX
            )
            chatChannel.enableLights(true)
            chatChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            chatChannel.enableVibration(true)
            getManager().createNotificationChannel(chatChannel)
        }
    }

    fun getChatMessageNotification(title: String, body: String): NotificationCompat.Builder {

        val requestID = System.currentTimeMillis().toInt()

        val intent = ChatActivity.getCallingIntent(context, title, body, null)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                requestID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(context, CHAT_CHANNEL)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setContentText(body)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
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

    fun cancellAllNotifications() {
        getManager().cancelAll()
    }
}