package com.divercity.android.features.linkedin.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.model.user.User
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
) : UseCase<User, LoginLinkedInUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<User> {
        return registerLoginRepository.loginLinkedin(params.code, params.state)
    }

    abstract class Callback : DisposableObserverWrapper<User>() {

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
