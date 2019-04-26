package com.divercity.android.features.industry.onboarding.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.user.User
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
) : UseCase<User, FollowIndustriesUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<User> {
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
