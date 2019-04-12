package com.divercity.android.features.jobs.savedjobs.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchSavedJobsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: JobRepository
) : UseCase<List<JobResponse>, Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<JobResponse>> {
        return repository.fetchSavedJobs(params.page, params.size, params.query)
    }

}
