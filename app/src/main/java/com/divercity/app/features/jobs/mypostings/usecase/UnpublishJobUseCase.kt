package com.divercity.app.features.jobs.mypostings.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class UnpublishJobUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<JobResponse, UnpublishJobUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<JobResponse> {
        return repository.publishUnpublishJob(params.jobId, "unpublish")
    }

    class Params private constructor(val jobId: String) {

        companion object {

            fun forPublish(jobId: String): Params {
                return Params(jobId)
            }
        }
    }
}
