package com.divercity.android.features.company.companydetail.employees.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.company.companydetail.employees.usecase.FetchEmployeesUseCase

class EmployeesDataSourceFactory(
    private val fetchMyCompaniesUseCase: FetchEmployeesUseCase
) : DataSource.Factory<Long, UserResponse>() {

    val employeesDataSource = MutableLiveData<EmployeesDataSource>()

    override fun create(): DataSource<Long, UserResponse> {

        val myCompaniesDataSource = EmployeesDataSource(fetchMyCompaniesUseCase)

        employeesDataSource.postValue(myCompaniesDataSource)
        return myCompaniesDataSource
    }

}
