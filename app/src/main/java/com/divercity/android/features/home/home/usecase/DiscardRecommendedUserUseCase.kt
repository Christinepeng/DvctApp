package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.user.discard.DiscardConnectionBody
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DiscardRecommendedUserUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<Unit, DiscardRecommendedUserUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.discardRecommendedConnection(DiscardConnectionBody(params.userIds))
    }

    class Params private constructor(val userIds: List<String>) {

        companion object {

            fun toDiscard(userIds: List<String>): Params {
                return Params(userIds)
            }
        }
    }
}
