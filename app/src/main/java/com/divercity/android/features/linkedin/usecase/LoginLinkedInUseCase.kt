package com.divercity.android.features.linkedin.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.repository.registerlogin.RegisterLoginRepository
import com.google.gson.JsonElement
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class LoginLinkedInUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val registerLoginRepository: RegisterLoginRepository
) : UseCase<UserResponse, LoginLinkedInUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<UserResponse> {
        return registerLoginRepository.loginLinkedin(params.code, params.state)
    }

    abstract class Callback : DisposableObserverWrapper<UserResponse>() {

        override fun onHttpException(error: JsonElement) {
            onFail(error.asJsonObject.getAsJsonArray("errors").get(0).asString)
        }
    }

    class Params private constructor(val code: String, val state: String) {

        companion object {

            fun forLogin(code: String, state: String): Params {
                return Params(code, state)
            }
        }
    }
}
