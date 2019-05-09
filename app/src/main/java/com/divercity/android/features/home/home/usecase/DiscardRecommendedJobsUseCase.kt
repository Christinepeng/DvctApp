package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DiscardRecommendedJobsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: JobRepository
) : UseCase<Unit, DiscardRecommendedJobsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.discardRecommendedJobs(params.jobIds)
    }

    class Params private constructor(val jobIds: List<String>) {

        companion object {

            fun toDiscard(jobIds: List<String>): Params {
                return Params(jobIds)
            }
        }
    }
}
