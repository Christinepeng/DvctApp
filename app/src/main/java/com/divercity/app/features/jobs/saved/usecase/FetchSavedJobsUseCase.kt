package com.divercity.app.features.jobs.saved.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.base.IncludedArray
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchSavedJobsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<IncludedArray<JobResponse>, FetchSavedJobsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<IncludedArray<JobResponse>> {
        return repository.fetchSavedJobs(params.page, params.size, params.query)
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forJobs(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}
