package com.divercity.android

import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.usecase.UpdateFCMTokenUseCase
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by lucas on 14/01/2019.
 */

class Session
constructor(
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository,
    private val updateFCMTokenUseCase: UpdateFCMTokenUseCase
) {

    fun logout() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            chatRepository.deleteRecentChatsDB()
            chatRepository.deleteChatMessagesDB()
            withContext(Dispatchers.IO) {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            }
        }

//        I DON'T KNOW WHY THE APP CRASH IF I CALL CLEARUSERDATA AFTER CALLING EXECUTE
        if (!sessionRepository.getDeviceId().isNullOrEmpty() && !sessionRepository.getFCMToken().isNullOrEmpty()) {
            val callback = object : DisposableObserverWrapper<Boolean>() {
                override fun onFail(error: String) {
                    sessionRepository.clearUserData()
                }

                override fun onHttpException(error: JsonElement) {
                    sessionRepository.clearUserData()

                }

                override fun onSuccess(o: Boolean) {
                    sessionRepository.clearUserData()
                }
            }
            updateFCMTokenUseCase.execute(
                callback, UpdateFCMTokenUseCase.Params.forDevice(
                    sessionRepository.getDeviceId()!!,
                    sessionRepository.getFCMToken()!!,
                    false
                )
            )
        } else {
            sessionRepository.clearUserData()
        }
    }
}