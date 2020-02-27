package com.divercity.android.features.password.changepassword.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class ChangePasswordUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<Unit, ChangePasswordUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.changePassword(
//            params.oldPassword,
            params.newPassword,
            params.confirmation
        )
    }

    class Params private constructor(
//        val oldPassword: String,
        val newPassword: String,
        val confirmation: String
    ) {

        companion object {

            fun to(
//                oldPassword: String,
                newPassword: String,
                confirmation: String
            ): Params {
                return Params(
//                    oldPassword,
                    newPassword,
                    confirmation
                )
            }
        }
    }
}
