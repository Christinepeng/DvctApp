package com.divercity.android.features.dialogs.jobapply.usecase

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

class ApplyToJobUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: JobRepository
) : UseCase<JobApplicationResponse, ApplyToJobUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<JobApplicationResponse> {
        return repository.applyJob(params.jobId, params.userDocId, params.coverLetter)
    }

    class Params private
    constructor(val jobId: String, val userDocId: String, val coverLetter: String) {

        companion object {

            fun forJob(jobId: String, userDocId: String, coverLetter: String): Params {
                return Params(jobId, userDocId, coverLetter)
            }
        }
    }
}
