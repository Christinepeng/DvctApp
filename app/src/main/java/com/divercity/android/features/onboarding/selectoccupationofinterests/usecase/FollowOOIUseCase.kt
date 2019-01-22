package com.divercity.android.features.onboarding.selectoccupationofinterests.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FollowOOIUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: UserRepository
) : UseCase<UserResponse, FollowOOIUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<UserResponse> {
        return repository.followOccupationOfInterests(params.ooiIds)
    }

    class Params private constructor(val ooiIds: List<String>) {

        companion object {

            fun forOOI(ooiIds: List<String>): Params {
                return Params(ooiIds)
            }
        }
    }
}
