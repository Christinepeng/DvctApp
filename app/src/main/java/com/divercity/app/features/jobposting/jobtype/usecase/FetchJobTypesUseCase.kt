package com.divercity.app.features.jobposting.jobtype.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchJobTypesUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<List<JobTypeResponse>, Any>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Any): Observable<List<JobTypeResponse>> {
        return repository.fetchJobTypes()
    }
}
