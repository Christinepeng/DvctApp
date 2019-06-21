package com.divercity.android.features.home.home.recommended

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchFeedRecommendedJobsGroupsNewUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val dataRepository: DataRepository
) : UseCase<@JvmSuppressWildcards List<RecommendedItem>, Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<RecommendedItem>> {

        return dataRepository.fetchRecommendedJobsGOIS(params.page, params.size).map {
            val jobs = it.jobs!!
            val groups = it.goi!!
            val shuffledRecommendedJobsGroups = ArrayList<RecommendedItem>()
            var startLimitJobs = 0
            var startLimitGroups = 0

            while (jobs.isNotEmpty() && startLimitJobs < jobs.size ||
                groups.isNotEmpty() && startLimitGroups < groups.size
            ) {
                if (startLimitJobs < jobs.size) {
                    val randJobs = (1..2).random()
                    var endLimitJobs = startLimitJobs + randJobs
                    if (endLimitJobs > jobs.size) endLimitJobs = jobs.size
                    for (i in startLimitJobs until endLimitJobs)
                        shuffledRecommendedJobsGroups.add(jobs[i])
                    startLimitJobs += randJobs
                }

                if (startLimitGroups < groups.size) {
                    val randQuestions = (1..2).random()
                    var endLimitQuestions = randQuestions + startLimitGroups
                    if (endLimitQuestions > groups.size) endLimitQuestions = groups.size
                    for (i in startLimitGroups until endLimitQuestions)
                        shuffledRecommendedJobsGroups.add(groups[i])
                    startLimitGroups += randQuestions
                }
            }
            return@map shuffledRecommendedJobsGroups
        }
    }
}