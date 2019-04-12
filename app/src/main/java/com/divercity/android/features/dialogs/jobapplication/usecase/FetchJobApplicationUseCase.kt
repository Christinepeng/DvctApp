package com.divercity.android.features.dialogs.jobapplication.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchJobApplicationUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository
) : UseCase<JobApplicationResponse, FetchJobApplicationUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<JobApplicationResponse> {
        return repository.fetchJobApplication(params.jobId)
    }

    class Params private constructor(val jobId: String) {

        companion object {

            fun forApplication(jobId: String): Params {
                return Params(jobId)
            }
        }
    }
}
