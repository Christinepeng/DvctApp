package com.divercity.android.features.password.resetpassword.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.registerlogin.RegisterLoginRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class ResetPasswordUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: RegisterLoginRepository
) : UseCase<Unit, ResetPasswordUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.resetPassword(params.password, params.token)
    }

    class Params private constructor(val password : String, val token : String) {

        companion object {

            fun to(password : String, token: String): Params {
                return Params(
                    password,
                    token
                )
            }
        }
    }
}
