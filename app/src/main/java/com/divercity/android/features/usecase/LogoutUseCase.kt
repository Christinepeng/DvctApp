package com.divercity.android.features.usecase

import com.divercity.android.db.AppDatabase
import com.divercity.android.repository.session.SessionRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 14/01/2019.
 */

class LogoutUseCase @Inject
constructor(
    private val appDatabase: AppDatabase,
    private val sessionRepository: SessionRepository,
    private val updateFCMTokenUseCase: UpdateFCMTokenUseCase
) {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun execute(onFinish: () -> Unit) {
        Timber.e("En logout 1")
        FirebaseMessaging.getInstance().isAutoInitEnabled = false

        ioScope.launch {
            Timber.e("En logout 2")
            appDatabase.clearAllTables()
            FirebaseInstanceId.getInstance().deleteInstanceId()

            if (!sessionRepository.getDeviceId().isNullOrEmpty() &&
                !sessionRepository.getFCMToken().isNullOrEmpty() &&
                sessionRepository.isUserLogged()
            ) {
                Timber.e("En logout 3")

                val callback = object : DisposableObserver<Boolean>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: Boolean) {
                        Timber.e("En logout 5")

                        ioScope.launch {
                            end(onFinish)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Timber.e("En logout 6")

                        ioScope.launch {
                            end(onFinish)
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
                Timber.e("En logout 4")
                end(onFinish)
            }
        }
    }

    private suspend fun end(onFinish: () -> Unit) {
        sessionRepository.clearUserData()
        withContext(Dispatchers.Main) {
            onFinish()
        }
    }

    fun cancel() {
        updateFCMTokenUseCase.dispose()
        ioScope.coroutineContext.cancelChildren()
    }
}