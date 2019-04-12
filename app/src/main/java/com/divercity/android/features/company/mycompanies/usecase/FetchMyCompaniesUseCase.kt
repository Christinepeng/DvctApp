package com.divercity.android.features.company.mycompanies.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.repository.company.CompanyRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchMyCompaniesUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository
) : UseCase<@JvmSuppressWildcards List<CompanyResponse>, FetchMyCompaniesUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<CompanyResponse>> {

        return repository.fetchCompaniesIamAdmin(
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