package com.divercity.android.features.login.step2.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.registerlogin.RegisterLoginRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class RequestResetPasswordUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: RegisterLoginRepository
) : UseCase<Unit, RequestResetPasswordUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.requestResetPassword(params.email)
    }

    class Params private constructor(val email : String) {

        companion object {

            fun to(email : String): Params {
                return Params(email)
            }
        }
    }
}
