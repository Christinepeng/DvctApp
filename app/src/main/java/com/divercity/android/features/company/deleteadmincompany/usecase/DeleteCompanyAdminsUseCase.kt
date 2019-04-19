package com.divercity.android.features.company.deleteadmincompany.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.company.CompanyRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeleteCompanyAdminsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository
) : UseCase<Unit, DeleteCompanyAdminsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.deleteCompanyAdmins(params.companyId, params.adminsId)
    }

    class Params constructor(val companyId: String, val adminsId: List<String>)
}
