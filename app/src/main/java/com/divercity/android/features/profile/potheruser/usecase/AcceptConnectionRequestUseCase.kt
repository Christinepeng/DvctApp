package com.divercity.android.features.profile.potheruser.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class AcceptConnectionRequestUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<ConnectUserResponse, AcceptConnectionRequestUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<ConnectUserResponse> {
        return repository.acceptConnectionRequest(params.userId)
    }

    class Params private constructor(val userId: String) {

        companion object {

            fun toAccept(userId: String): Params {
                return Params(
                    userId
                )
            }
        }
    }
}
