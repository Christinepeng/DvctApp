package com.divercity.android.features.company.companydetail.employees.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.company.CompanyRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchEmployeesUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: CompanyRepository
) : UseCase<@JvmSuppressWildcards List<User>, Params>(executorThread, uiThread) {

    lateinit var companyId: String

    override fun createObservableUseCase(params: Params): Observable<List<User>> {
        return repository.fetchEmployeesByCompany(companyId, params.page, params.size)
    }
}
