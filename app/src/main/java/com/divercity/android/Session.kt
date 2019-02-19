package com.divercity.android

import android.content.Context
import com.divercity.android.features.usecase.UpdateFCMTokenUseCase
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by lucas on 14/01/2019.
 */

class Session
constructor(
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository,
    private val updateFCMTokenUseCase: UpdateFCMTokenUseCase,
    private val context : Context
) {

    fun logout() {
        Timber.e("En logout")
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            chatRepository.deleteRecentChatsDB()
            chatRepository.deleteChatMessagesDB()
            withContext(Dispatchers.IO) {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            }
        }

        if (!sessionRepository.getDeviceId().isNullOrEmpty() &&
            !sessionRepository.getFCMToken().isNullOrEmpty() &&
            sessionRepository.isUserLogged()
        ) {
            val callback = object : DisposableObserver<Boolean>() {
                override fun onComplete() {
                    sessionRepository.clearUserData()
                }

                override fun onNext(t: Boolean) {
                    sessionRepository.clearUserData()
                }

                override fun onError(e: Throwable) {
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