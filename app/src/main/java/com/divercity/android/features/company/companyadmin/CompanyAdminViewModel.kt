package com.divercity.android.features.company.companyadmin

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAdminViewModel @Inject
constructor(repository: CompanyAdminPaginatedRepository) :
    BaseViewModelPagination<CompanyAdminResponse>(repository) {

    fun fetchCompanyAdmins(companyId: String) {
        (repository as CompanyAdminPaginatedRepository).setCompanyId(companyId)
        fetchData()
    }
}
