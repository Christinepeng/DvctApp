package com.divercity.android.features.company.companydetail.usecase

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

class FetchCompanyUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository
) : UseCase<CompanyResponse, FetchCompanyUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<CompanyResponse> {
        return repository.fetchCompany(
            params.companyId
        )
    }

    class Params constructor(val companyId: String)
}