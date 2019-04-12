package com.divercity.android.features.company.companyadmin.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
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
) : UseCase<@JvmSuppressWildcards List<CompanyAdminResponse>, Params>(
    executorThread,
    uiThread
) {

    lateinit var companyId: String

    override fun createObservableUseCase(params: Params): Observable<List<CompanyAdminResponse>> {
        return repository.fetchCompanyAdmins(
            companyId,
            params.page,
            params.size
        )
    }
}