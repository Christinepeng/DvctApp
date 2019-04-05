package com.divercity.android.services

import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.usecase.UpdateFCMTokenUseCase
import com.divercity.android.helpers.NotificationHelper
import com.divercity.android.repository.session.SessionRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonElement
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * Created by lucas on 15/01/2019.
 */

class DivercityMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var updateFCMTokenUseCase: UpdateFCMTokenUseCase

    @Inject
    lateinit var sessionRepository: SessionRepository

    companion object {
        const val NEW_DIRECT_MESSAGE = "NEW_DIRECT_MESSAGE"
        const val NEW_CONNECTION_REQUEST = "NEW_CONNECTION_REQUEST"
        const val NEW_CONNECTION_ACCEPT = "NEW_CONNECTION_ACCEPT"
        const val NEW_GROUP_INVITE_PN = "NEW_GROUP_INVITE_PN"
        const val GROUP_JOIN_REQUEST = "GROUP_JOIN_REQUEST"
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onMessageReceived(data: RemoteMessage?) {
        data?.let {
            if (data.data.isNotEmpty()) {
                when (data.data["category"]) {
                    NEW_DIRECT_MESSAGE -> {
                        /* This is to not show notification when user is on the chat screen*/

                        if (sessionRepository.getCurrentChatId() != data.data["chat_id"]) {
                            RxBus.publish(RxEvent.OnNewMessageReceived(true))
                            notificationHelper.notify(
                                data.data["chat_id"]!!.toInt(),
                                notificationHelper
                                    .getChatMessageNotification(
                                        "New message",
                                        data.data["alert"]!!,
                                        data.data["sender_id"],
                                        data.data["chat_id"]!!.toInt(),
                                        data.data["display_name"]
                                    )
                            )
                        }
                    }
                    NEW_CONNECTION_REQUEST -> {
                        notificationHelper.notify(
                            Math.random().toInt(),
                            notificationHelper
                                .getRequestNotification(
                                    "New connection request",
                                    data.data["alert"]!!,
                                    data.data["follower_id"]
                                )
                        )
                    }
                    NEW_CONNECTION_ACCEPT -> {
                        notificationHelper.notify(
                            Math.random().toInt(),
                            notificationHelper
                                .getRequestNotification(
                                    "Connection request accepted",
                                    data.data["alert"]!!,
                                    data.data["accepted_by"]
                                )
                        )
                    }
                    NEW_GROUP_INVITE_PN -> {
                        notificationHelper.notify(
                            Math.random().toInt(),
                            notificationHelper
                                .getGroupInviteJoinRequestNotification(
                                    "Group invitation",
                                    data.data["alert"]!!,
                                    data.data["goi_id"]!!
                                )
                        )
                    }
                    GROUP_JOIN_REQUEST -> {
                        notificationHelper.notify(
                            Math.random().toInt(),
                            notificationHelper
                                .getGroupInviteJoinRequestNotification(
                                    "Group join request",
                                    data.data["alert"]!!,
                                    data.data["goi_id"]!!
                                )
                        )
                    }
                }
            }
        }
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        sessionRepository.setFCMToken(p0)
        sessionRepository.getDeviceId()?.let { deviceId ->
            p0?.let { token ->
                val callback = object : DisposableObserverWrapper<Boolean>() {
                    override fun onFail(error: String) {
                    }

                    override fun onHttpException(error: JsonElement) {
                    }

                    override fun onSuccess(o: Boolean) {
                    }
                }
                updateFCMTokenUseCase.execute(
                    callback,
                    UpdateFCMTokenUseCase.Params.forDevice(deviceId, token, true)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateFCMTokenUseCase.dispose()
    }
}

