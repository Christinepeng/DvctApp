package com.divercity.android

import com.divercity.android.features.usecase.UpdateFCMTokenUseCase
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Created by lucas on 14/01/2019.
 */

class Session
constructor(
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository,
    private val updateFCMTokenUseCase: UpdateFCMTokenUseCase
) {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun logout(onFinish: () -> Unit) {
        Timber.e("En logout")
        FirebaseMessaging.getInstance().isAutoInitEnabled = false

        ioScope.launch {
            chatRepository.deleteRecentChatsDB()
            chatRepository.deleteChatMessagesDB()
            FirebaseInstanceId.getInstance().deleteInstanceId()

            if (!sessionRepository.getDeviceId().isNullOrEmpty() &&
                !sessionRepository.getFCMToken().isNullOrEmpty() &&
                sessionRepository.isUserLogged()
            ) {
                val callback = object : DisposableObserver<Boolean>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: Boolean) {
                        ioScope.launch {
                            sessionRepository.clearUserData()
                            withContext(Dispatchers.Main) {
                                onFinish()
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        ioScope.launch {
                            sessionRepository.clearUserData()
                            withContext(Dispatchers.Main) {
                                onFinish()
                            }
                        }
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
                withContext(Dispatchers.Main) {
                    onFinish()
                }
            }
        }
    }

    fun cancel() {
        updateFCMTokenUseCase.dispose()
        ioScope.coroutineContext.cancelChildren()
    }
}