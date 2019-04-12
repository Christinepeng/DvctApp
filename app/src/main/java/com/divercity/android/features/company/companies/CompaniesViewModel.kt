package com.divercity.android.features.company.companies

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.selectcompany.base.CompanyPaginatedRepository
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class CompaniesViewModel @Inject
constructor(repository: CompanyPaginatedRepository) :
    BaseViewModelPagination<CompanyResponse>(repository) {

    init {
        fetchData(null, "")
    }
}
