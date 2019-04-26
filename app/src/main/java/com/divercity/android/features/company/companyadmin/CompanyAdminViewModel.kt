package com.divercity.android.features.company.companyadmin

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminEntityResponse
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAdminViewModel @Inject
constructor(repository: CompanyAdminPaginatedRepository) :
    BaseViewModelPagination<CompanyAdminEntityResponse>(repository) {

    fun fetchCompanyAdmins(companyId: String) {
        (repository as CompanyAdminPaginatedRepository).setCompanyId(companyId)
        fetchData()
    }
}
