package com.divercity.app.features.jobs.mypostings.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.repository.job.JobRepository
import com.divercity.app.repository.user.UserRepository
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
            private val userRepository: UserRepository
) : UseCase<@JvmSuppressWildcards List<JobResponse>, FetchJobsPostingsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<JobResponse>> {
        return repository.fetchJobPostingsByUser(userRepository.getUserId()!!,
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
