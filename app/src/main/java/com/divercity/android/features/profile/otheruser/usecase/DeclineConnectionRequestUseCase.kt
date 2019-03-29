package com.divercity.android.features.profile.otheruser.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeclineConnectionRequestUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<Unit, DeclineConnectionRequestUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.declineConnectionRequest(params.userId)
    }

    class Params private constructor(val userId: String) {

        companion object {

            fun toDecline(userId: String): Params {
                return Params(
                    userId
                )
            }
        }
    }
}
