package com.divercity.android.features.company.companyadmin.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.divercity.android.data.entity.company.companyadmin.CompanyAdminResponse
import com.divercity.android.features.company.companyadmin.usecase.FetchCompanyAdminsUseCase

class CompanyAdminSourceFactory(
    private val fetchCompanyAdminsUseCase: FetchCompanyAdminsUseCase,
    private val companyId: String
) : DataSource.Factory<Long, CompanyAdminResponse>() {

    val dataSource = MutableLiveData<CompanyAdminDataSource>()

    override fun create(): DataSource<Long, CompanyAdminResponse> {

        val companyAdminDataSource = CompanyAdminDataSource(
            fetchCompanyAdminsUseCase,
            companyId
        )

        dataSource.postValue(companyAdminDataSource)
        return companyAdminDataSource
    }
}
