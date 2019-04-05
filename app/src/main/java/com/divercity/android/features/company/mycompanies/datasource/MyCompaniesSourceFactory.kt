package com.divercity.android.features.company.mycompanies.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.mycompanies.usecase.FetchMyCompaniesUseCase

class MyCompaniesSourceFactory(
    private val fetchMyCompaniesUseCase : FetchMyCompaniesUseCase
) : DataSource.Factory<Long, CompanyResponse>() {

    val connectionsDataSource = MutableLiveData<MyCompaniesDataSource>()

    override fun create(): DataSource<Long, CompanyResponse> {

        val myCompaniesDataSource = MyCompaniesDataSource(
            fetchMyCompaniesUseCase
        )

        connectionsDataSource.postValue(myCompaniesDataSource)
        return myCompaniesDataSource
    }

}
