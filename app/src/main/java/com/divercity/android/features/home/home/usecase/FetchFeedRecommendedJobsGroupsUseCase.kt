package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.data.entity.home.Recommended
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.repository.group.GroupRepository
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function4
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
    private val feedRepository: GroupRepository
) : UseCase<@JvmSuppressWildcards List<HomeItem>, FetchFeedRecommendedJobsGroupsUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<HomeItem>> {

        val fetchRecommendedJobs = jobRepository.fetchRecommendedJobs(
            0,
            5,
            null
        )

        val fetchGroups = groupRepository.fetchRecommendedGroups(
            0,
            5
        )

        val fetchQuestions = feedRepository.fetchFeedQuestions(
            params.page,
            params.size
        )

        val fetchJobs = jobRepository.fetchJobs(
            params.page,
            params.size,
            null
        )

        return Observable.zip(
            fetchGroups,
            fetchRecommendedJobs,
            fetchQuestions,
            fetchJobs,
            Function4 { t1, t2, questions, jobs ->
                val recommendedRes = ArrayList<RecommendedItem>()
                recommendedRes.addAll(t2)
                recommendedRes.addAll(t1)
                recommendedRes.shuffle()

                val recommended = Recommended(recommendedRes)

                val shuffledJobsAndQuestions = ArrayList<HomeItem>()
                var startLimitJobs = 0
                var startLimitQuestions = 0

                while(jobs.isNotEmpty() && startLimitJobs < jobs.size ||
                    questions.isNotEmpty() && startLimitQuestions < questions.size){

                    if(startLimitJobs < jobs.size){
                        val randJobs = (1..2).random()
                        var endLimitJobs = startLimitJobs + randJobs
                        if(endLimitJobs > jobs.size) endLimitJobs = jobs.size
                        for(i in startLimitJobs..(endLimitJobs - 1))
                            shuffledJobsAndQuestions.add(jobs[i])
                        startLimitJobs += randJobs
                    }

                    if(startLimitQuestions < questions.size){
                        val randQuestions = (1..2).random()
                        var endLimitQuestions = randQuestions + startLimitQuestions
                        if(endLimitQuestions > questions.size) endLimitQuestions = questions.size
                        for(i in startLimitQuestions..(endLimitQuestions - 1))
                            shuffledJobsAndQuestions.add(questions[i])
                        startLimitQuestions += randQuestions
                    }
                }

                val res = ArrayList<HomeItem>()
                res.add(recommended)
                res.addAll(shuffledJobsAndQuestions)

                return@Function4 res
            }
        )
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forJobs(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}