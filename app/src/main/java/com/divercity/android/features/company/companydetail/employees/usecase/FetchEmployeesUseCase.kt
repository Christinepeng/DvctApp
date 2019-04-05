package com.divercity.android.features.company.companydetail.employees.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
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
) : UseCase<@JvmSuppressWildcards List<UserResponse>, FetchEmployeesUseCase.Params>(executorThread, uiThread) {

    lateinit var companyId: String

    override fun createObservableUseCase(params: Params): Observable<List<UserResponse>> {
        return repository.fetchEmployeesByCompany(companyId, params.page, params.size)
    }

    class Params private constructor(val page: Int, val size: Int) {

        companion object {

            fun forFollowers(page: Int, size: Int): Params {
                return Params(page, size)
            }
        }
    }
}
