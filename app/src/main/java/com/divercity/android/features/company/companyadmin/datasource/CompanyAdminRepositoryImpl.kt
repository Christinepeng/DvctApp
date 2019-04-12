package com.divercity.android.features.company.companyadmin.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.features.company.companyadmin.usecase.FetchCompanyAdminsUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class CompanyAdminRepositoryImpl @Inject
internal constructor(private val fetchCompanyAdminsUseCase: FetchCompanyAdminsUseCase) {

    var companyAdminSourceFactory: CompanyAdminSourceFactory? = null

    companion object {

        const val pageSize = 20
    }

    fun fetchData(companyId: String): Listing<CompanyAdminResponse> {

        val executor = Executors.newFixedThreadPool(5)

        companyAdminSourceFactory = CompanyAdminSourceFactory(
            fetchCompanyAdminsUseCase,
            companyId
        )

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(companyAdminSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(companyAdminSourceFactory!!.dataSource) { input -> input.networkState },
            Transformations.switchMap(companyAdminSourceFactory!!.dataSource) { input -> input.initialLoad }
        )
    }

    fun retry() {
        companyAdminSourceFactory?.dataSource?.value?.retry()
    }

    fun refresh() {
        companyAdminSourceFactory?.dataSource?.value?.invalidate()
    }

    fun clear() {
        companyAdminSourceFactory?.dataSource?.value?.dispose()
    }
}
