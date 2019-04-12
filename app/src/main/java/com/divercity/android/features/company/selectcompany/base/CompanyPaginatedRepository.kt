package com.divercity.android.features.company.selectcompany.base

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.selectcompany.base.usecase.FetchCompaniesUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class CompanyPaginatedRepository @Inject
internal constructor(private val fetchCompaniesUseCase: FetchCompaniesUseCase) :
    BaseDataSourceRepository<CompanyResponse>() {

    override fun getUseCase(): UseCase<List<CompanyResponse>, Params> {
        return fetchCompaniesUseCase
    }
}
