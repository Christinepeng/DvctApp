package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.repository.group.GroupRepository
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
    private val feedRepository: GroupRepository
) : UseCase<@JvmSuppressWildcards List<HomeItem>, Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<HomeItem>> {

        val fetchJobs = jobRepository.fetchJobs(
            params.page,
            params.size,
            null
        )

        val fetchQuestions = feedRepository.fetchFeedQuestions(
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
                        for(i in startLimitJobs until endLimitJobs)
                            shuffledJobsAndQuestions.add(jobs[i])
                        startLimitJobs += randJobs
                    }

                    if(startLimitQuestions < questions.size){
                        val randQuestions = (1..2).random()
                        var endLimitQuestions = randQuestions + startLimitQuestions
                        if(endLimitQuestions > questions.size) endLimitQuestions = questions.size
                        for(i in startLimitQuestions until endLimitQuestions)
                            shuffledJobsAndQuestions.add(questions[i])
                        startLimitQuestions += randQuestions
                    }
                }

                return@BiFunction shuffledJobsAndQuestions
            }
        )
    }
}
