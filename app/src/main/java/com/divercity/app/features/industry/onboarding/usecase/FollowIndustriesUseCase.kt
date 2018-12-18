package com.divercity.app.features.industry.onboarding.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.repository.user.UserRepository
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
) : UseCase<LoginResponse, FollowIndustriesUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<LoginResponse> {
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
