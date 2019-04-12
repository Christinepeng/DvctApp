package com.divercity.android.features.jobs.mypostings.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.repository.job.JobRepository
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchJobsPostingsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: JobRepository,
            private val sessionRepository: SessionRepository
) : UseCase<@JvmSuppressWildcards List<JobResponse>, FetchJobsPostingsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<JobResponse>> {
        return repository.fetchJobPostingsByUser(sessionRepository.getUserId(),
                params.page, params.size, if(params.query != null && params.query == "") null else params.query)
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forJobs(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}
