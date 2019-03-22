package com.divercity.android.helpers

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.divercity.android.R
import com.divercity.android.features.chat.chat.ChatActivity
import com.divercity.android.features.profile.otheruser.OtherUserProfileActivity
import com.facebook.FacebookSdk.getApplicationContext
import javax.inject.Inject


/**
 * Created by lucas on 14/01/2019.
 */

class NotificationHelper @Inject
constructor(val context: Context) {

    val CHAT_CHANNEL = "chats"
    val GENERAL_NOTIFICATIONS = "general_notifications"
    private var manager: NotificationManager? = null

    init {
        createChannels()
    }

    @SuppressLint("WrongConstant")
    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//          Chat channel
            val chatChannel = NotificationChannel(
                CHAT_CHANNEL,
                context.getString(R.string.noti_channel_chats),
                NotificationManager.IMPORTANCE_MAX
            )
            chatChannel.enableLights(true)
            chatChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            chatChannel.enableVibration(true)
            getManager().createNotificationChannel(chatChannel)

//          General channel
            val generalChannel = NotificationChannel(
                GENERAL_NOTIFICATIONS,
                context.getString(R.string.noti_channel_general),
                NotificationManager.IMPORTANCE_MAX
            )
            chatChannel.enableLights(true)
            chatChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            chatChannel.enableVibration(true)
            getManager().createNotificationChannel(generalChannel)
        }
    }

    fun getChatMessageNotification(
        title: String,
        body: String,
        userId: String?,
        chatId: Int,
        nameDisplay: String?
    ): NotificationCompat.Builder {

        val requestID = System.currentTimeMillis().toInt()

        val intent = ChatActivity.getCallingIntent(context, nameDisplay ?: "", userId, chatId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val resultPendingIntent = PendingIntent.getActivity(
            getApplicationContext(),
            requestID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Builder(context, CHAT_CHANNEL)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setBigContentTitle(title)
            )
            .setContentIntent(resultPendingIntent)
            .setContentText(body)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setSmallIcon(getSmallIcon())
            .setAutoCancel(true)
    }

    fun getRequestNotification(
        title: String,
        body: String,
        userId: String?
    ): NotificationCompat.Builder {

        val requestID = System.currentTimeMillis().toInt()

        val intent = OtherUserProfileActivity.getCallingIntent(context, userId, null)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val resultPendingIntent = PendingIntent.getActivity(
            getApplicationContext(),
            requestID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, GENERAL_NOTIFICATIONS)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setBigContentTitle(title)
            )
            .setContentIntent(resultPendingIntent)
            .setContentText(body)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(getSmallIcon())
            .setAutoCancel(true)
    }

    fun notify(id: Int, notification: NotificationCompat.Builder) {
        getManager().notify(id, notification.build())
    }

    private fun getSmallIcon(): Int {
        return R.drawable.icon_notification
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