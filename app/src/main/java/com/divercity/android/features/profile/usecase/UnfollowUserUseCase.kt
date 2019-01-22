package com.divercity.android.features.profile.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class UnfollowUserUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<Void, UnfollowUserUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Void> {
        return repository.unfollowUser(params.userId)
    }

    class Params private constructor(val userId: String) {

        companion object {

            fun toUnfollow(userId: String): Params {
                return Params(userId)
            }
        }
    }
}
