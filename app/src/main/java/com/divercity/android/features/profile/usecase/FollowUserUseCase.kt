package com.divercity.android.features.profile.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.user.followuser.FollowUserResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FollowUserUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<FollowUserResponse, FollowUserUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<FollowUserResponse> {
        return repository.followUser(params.userId)
    }

    class Params private constructor(val userId: String) {

        companion object {

            fun toFollow(userId: String): Params {
                return Params(userId)
            }
        }
    }
}
