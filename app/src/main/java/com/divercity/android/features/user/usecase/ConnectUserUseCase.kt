package com.divercity.android.features.user.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class ConnectUserUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<ConnectUserResponse, ConnectUserUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<ConnectUserResponse> {
        return repository.connectUser(params.userId)
    }

    class Params private constructor(val userId: String) {

        companion object {

            fun toFollow(userId: String): Params {
                return Params(userId)
            }
        }
    }
}