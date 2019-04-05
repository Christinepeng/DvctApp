package com.divercity.android.features.company.companydetail.employees.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.company.companydetail.employees.usecase.FetchEmployeesUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class EmployeesPaginatedRepositoryImpl @Inject
internal constructor(private val fetchEmployeesUseCase: FetchEmployeesUseCase) {

    private var employeesDataSourceFactory: EmployeesDataSourceFactory? = null

    companion object {

        const val pageSize = 20
    }

    fun fetchData(companyId: String): Listing<UserResponse> {

        val executor = Executors.newFixedThreadPool(5)

        fetchEmployeesUseCase.companyId = companyId

        employeesDataSourceFactory = EmployeesDataSourceFactory(fetchEmployeesUseCase)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(employeesDataSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(employeesDataSourceFactory!!.employeesDataSource) { input -> input.networkState },
            Transformations.switchMap(employeesDataSourceFactory!!.employeesDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() = employeesDataSourceFactory?.employeesDataSource?.value?.retry()


    fun refresh() = employeesDataSourceFactory?.employeesDataSource?.value?.invalidate()


    fun clear() = employeesDataSourceFactory?.employeesDataSource?.value?.dispose()
}
