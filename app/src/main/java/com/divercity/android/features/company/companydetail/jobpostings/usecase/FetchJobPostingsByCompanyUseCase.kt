package com.divercity.android.features.company.companydetail.jobpostings.usecase

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

class FetchJobPostingsByCompanyUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: JobRepository
) : UseCase<@JvmSuppressWildcards List<JobResponse>, FetchJobPostingsByCompanyUseCase.Params>(
    executorThread,
    uiThread
) {

    lateinit var companyId: String

    override fun createObservableUseCase(params: Params): Observable<List<JobResponse>> {
        return repository.fetchJobsByCompany(
            companyId,
            params.page,
            params.size
        )
    }

    class Params private constructor(val page: Int, val size: Int) {

        companion object {

            fun forJobs(page: Int, size: Int): Params {
                return Params(page, size)
            }
        }
    }
}
