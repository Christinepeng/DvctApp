package com.divercity.android.features.company.companyadmin

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminEntityResponse
import com.divercity.android.features.company.companyadmin.usecase.FetchCompanyAdminsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class CompanyAdminPaginatedRepository @Inject
internal constructor(private val fetchCompanyAdminsUseCase: FetchCompanyAdminsUseCase) :
    BaseDataSourceRepository<CompanyAdminEntityResponse>() {

    override fun getUseCase(): UseCase<List<CompanyAdminEntityResponse>, Params> = fetchCompanyAdminsUseCase

    fun setCompanyId(companyId: String) {
        fetchCompanyAdminsUseCase.companyId = companyId
    }
}
