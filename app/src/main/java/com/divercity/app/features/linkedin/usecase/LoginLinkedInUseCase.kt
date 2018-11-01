package com.divercity.app.features.linkedin.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.repository.registerlogin.RegisterLoginRepository
import com.google.gson.JsonElement
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class LoginLinkedInUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val registerLoginRepository: RegisterLoginRepository
) : UseCase<LoginResponse, LoginLinkedInUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<LoginResponse> {
        return registerLoginRepository.loginLinkedin(params.code, params.state)
    }

    abstract class Callback : DisposableObserverWrapper<LoginResponse>() {

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
