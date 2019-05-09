package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.repository.group.GroupRepository
import com.divercity.android.repository.job.JobRepository
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchFeedRecommendedJobsGroupsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val jobRepository: JobRepository,
    private val groupRepository: GroupRepository,
    private val sessionRepository: SessionRepository
) : UseCase<@JvmSuppressWildcards List<RecommendedItem>, Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<RecommendedItem>> {

//        if (sessionRepository.isLoggedUserJobSeeker()) {

            val fetchRecommendedJobs = jobRepository.fetchRecommendedJobs(
                params.page,
                params.size,
                null
            )

            val fetchRecommendedGroups = groupRepository.fetchRecommendedGroups(
                params.page,
                params.size
            )

            return Observable.zip(
                fetchRecommendedGroups,
                fetchRecommendedJobs,
                BiFunction { groups, jobs ->
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
                    return@BiFunction shuffledRecommendedJobsGroups
                }
            )
//        } else {
//            @Suppress("UNCHECKED_CAST")
//            return groupRepository.fetchRecommendedGroups(
//                params.page,
//                params.size
//            ) as Observable<List<RecommendedItem>>
//        }
    }
}