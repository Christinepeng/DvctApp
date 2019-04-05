package com.divercity.android.features.company.companyadmin.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.company.companyadmin.CompanyAdminResponse
import com.divercity.android.repository.company.CompanyRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchCompanyAdminsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository
) : UseCase<@JvmSuppressWildcards List<CompanyAdminResponse>, FetchCompanyAdminsUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<CompanyAdminResponse>> {
        return repository.fetchCompanyAdmins(
            params.companyId,
            params.page,
            params.size
        )
    }

    class Params private constructor(val companyId: String, val page: Int, val size: Int) {

        companion object {

            fun forAdmin(companyId: String, page: Int, size: Int): Params {
                return Params(companyId, page, size)
            }
        }
    }
}