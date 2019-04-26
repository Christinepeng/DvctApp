package com.divercity.android.features.company.companydetail.employees

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.company.companydetail.employees.usecase.FetchEmployeesUseCase
import com.divercity.android.model.user.User
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class EmployeesPaginatedRepository @Inject
internal constructor(private val fetchEmployeesUseCase: FetchEmployeesUseCase) :
    BaseDataSourceRepository<User>() {

    override fun getUseCase(): UseCase<List<User>, Params> = fetchEmployeesUseCase

    fun setCompanyId(companyId: String) {
        fetchEmployeesUseCase.companyId = companyId
    }
}
