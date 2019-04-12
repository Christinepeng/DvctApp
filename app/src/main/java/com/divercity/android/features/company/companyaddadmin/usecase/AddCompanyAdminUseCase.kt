package com.divercity.android.features.company.companyaddadmin.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.companyadmin.body.AddAdminCompanyBody
import com.divercity.android.data.entity.company.companyadmin.body.Admin
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.repository.company.CompanyRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class AddCompanyAdminUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository
) : UseCase<@JvmSuppressWildcards List<CompanyAdminResponse>, AddCompanyAdminUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<CompanyAdminResponse>> {
        return repository.addCompanyAdmin(
            params.companyId,
            AddAdminCompanyBody(Admin(params.userIds))
        )
    }

    class Params private constructor(val companyId: String, val userIds: List<String>) {

        companion object {

            fun forAdmins(companyId: String, userIds: List<String>): Params {
                return Params(companyId, userIds)
            }
        }
    }
}