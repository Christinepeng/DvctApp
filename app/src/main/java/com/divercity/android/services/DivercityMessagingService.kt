package com.divercity.android.services

import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.usecase.UpdateFCMTokenUseCase
import com.divercity.android.helpers.NotificationHelper
import com.divercity.android.repository.user.UserRepository
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
    lateinit var userRepository: UserRepository

    companion object {
        const val NEW_DIRECT_MESSAGE = "NEW_DIRECT_MESSAGE"
        const val GROUP_INVITE = "GROUP_INVITE"
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
            }
        }
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        userRepository.setFCMToken(p0)
        userRepository.getDeviceId()?.let { deviceId ->
            p0?.let { token ->
                val callback = object : DisposableObserverWrapper<Void>() {
                    override fun onFail(error: String) {
                    }

                    override fun onHttpException(error: JsonElement) {
                    }

                    override fun onSuccess(o: Void) {
                    }
                }
                updateFCMTokenUseCase.execute(callback, UpdateFCMTokenUseCase.Params.forDevice(deviceId, token, true))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateFCMTokenUseCase.dispose()
    }
}

