package com.divercity.android.features.industry.onboarding.usecase

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

class FollowIndustriesUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<UserResponse, FollowIndustriesUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<UserResponse> {
        return repository.followIndustries(params.idsList)
    }

    class Params private constructor(val idsList: List<String>) {

        companion object {

            fun forIndustry(idsList: List<String>): Params {
                return Params(idsList)
            }
        }
    }
}
