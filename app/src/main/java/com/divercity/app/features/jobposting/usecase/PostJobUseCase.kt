package com.divercity.app.features.jobposting.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.job.jobpostingbody.JobBody
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class PostJobUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<DataObject<JobResponse>, PostJobUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<DataObject<JobResponse>> {
        return repository.postJob(JobBody())
    }

    class Params private constructor(val jobId: String) {

        companion object {

            fun forJob(jobId: String): Params {
                return Params(jobId)
            }
        }
    }
}
