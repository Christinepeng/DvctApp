package com.divercity.app.features.groups.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.recommendedgroups.RecommendedGroupsResponse
import com.divercity.app.repository.recommender.RecommenderRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchRecommendedGroupsUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: RecommenderRepository
) : UseCase<List<RecommendedGroupsResponse>, FetchRecommendedGroupsUseCase.Params>(
        executorThread,
        uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<RecommendedGroupsResponse>> {
        return repository.fetchRecommendedGroups(params.page, params.size)
    }

    class Params private constructor(
            val page: Int,
            val size: Int
    ) {

        companion object {

            fun forGroups(page: Int, size: Int): Params {
                return Params(page, size)
            }
        }
    }
}
