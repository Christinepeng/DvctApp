package com.divercity.android.features.activity.connectionrequests.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchConnectionRequestsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<@JvmSuppressWildcards List<ConnectionItem>, FetchConnectionRequestsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<ConnectionItem>> {
        val fetchQuestions = repository.fetchGroupInvitations(
            params.page,
            params.size
        )

        val fetchJobs = repository.fetchGroupJoinRequests(
            params.page,
            params.size
        )

        return Observable.zip(
            fetchQuestions,
            fetchJobs,
            BiFunction { t1, t2 ->
                return@BiFunction ArrayList()
            }
        )
    }

    class Params private constructor(val page: Int, val size: Int) {

        companion object {

            fun forConnRequest(page: Int, size: Int): Params {
                return Params(page, size)
            }
        }
    }
}
