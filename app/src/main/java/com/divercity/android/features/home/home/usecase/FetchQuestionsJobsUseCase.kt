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
            BiFunction { t1, t2 ->
                val res = ArrayList<HomeItem>()
                res.addAll(t1)
                res.addAll(t2)
                res.shuffle()

                return@BiFunction res
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