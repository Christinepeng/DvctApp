package com.divercity.android.features.jobs.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeleteJobUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<JobResponse, DeleteJobUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<JobResponse> {
        return repository.performActionJob(params.jobId, "delete")
    }

    class Params private constructor(val jobId: String) {

        companion object {

            fun forDelete(jobId: String): Params {
                return Params(jobId)
            }
        }
    }
}
