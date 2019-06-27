package com.divercity.android.features.company.selectcompany.base

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.company.response.CompanyResponse
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectCompanyViewModel @Inject
constructor(repository: CompanyPaginatedRepository)
    : BaseViewModelPagination<CompanyResponse>(repository)



