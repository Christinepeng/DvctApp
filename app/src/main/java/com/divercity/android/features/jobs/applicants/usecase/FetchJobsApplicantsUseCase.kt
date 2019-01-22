package com.divercity.android.features.jobs.applicants.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchJobsApplicantsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<@JvmSuppressWildcards List<JobApplicationResponse>, FetchJobsApplicantsUseCase.Params>(executorThread, uiThread) {

    lateinit var jobId: String

    override fun createObservableUseCase(params: Params): Observable<List<JobApplicationResponse>> {
        return repository.fetchApplicants(jobId, params.page, params.size)
    }

    class Params private constructor(val page: Int, val size: Int) {

        companion object {

            fun forApplicants(page: Int, size: Int): Params {
                return Params(page, size)
            }
        }
    }
}
