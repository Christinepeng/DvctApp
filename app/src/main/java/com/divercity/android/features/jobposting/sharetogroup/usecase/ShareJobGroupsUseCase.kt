package com.divercity.android.features.jobposting.sharetogroup.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.job.jobsharedgroupbody.JobShareGroupBody
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class ShareJobGroupsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<JobResponse, ShareJobGroupsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<JobResponse> {
        return repository.shareJob(params.jobId, JobShareGroupBody(params.listGroupIds))
    }

    class Params private constructor(val jobId: String, val listGroupIds: List<String>) {

        companion object {

            fun forShare(jobId: String, listGroupIds: List<String>): Params {
                return Params(jobId, listGroupIds)
            }
        }
    }
}
