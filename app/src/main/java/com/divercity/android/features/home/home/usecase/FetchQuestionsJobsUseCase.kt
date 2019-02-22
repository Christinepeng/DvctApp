package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.repository.feed.FeedRepository
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchQuestionsJobsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val jobRepository: JobRepository,
    private val feedRepository: FeedRepository
) : UseCase<@JvmSuppressWildcards List<HomeItem>, FetchQuestionsJobsUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<HomeItem>> {

        val fetchJobs = jobRepository.fetchJobs(
            params.page,
            params.size,
            null
        )

        val fetchQuestions = feedRepository.fetchQuestions(
            params.page,
            params.size
        )

        return Observable.zip(
            fetchJobs,
            fetchQuestions,
            BiFunction { jobs, questions ->
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

                return@BiFunction shuffledJobsAndQuestions
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
