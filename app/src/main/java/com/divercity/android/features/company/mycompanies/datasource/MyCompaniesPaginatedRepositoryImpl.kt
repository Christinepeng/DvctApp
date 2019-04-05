package com.divercity.android.features.company.mycompanies.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.PaginatedRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.mycompanies.usecase.FetchMyCompaniesUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class MyCompaniesPaginatedRepositoryImpl
@Inject internal constructor(
    private val fetchMyCompaniesUseCase: FetchMyCompaniesUseCase
) : PaginatedRepository<CompanyResponse> {

    private var connectionsSourceFactory: MyCompaniesSourceFactory? = null

    companion object {

        private val pageSize = 20
    }

    override fun fetchData(): Listing<CompanyResponse> {

        val executor = Executors.newFixedThreadPool(5)

        connectionsSourceFactory =
            MyCompaniesSourceFactory(fetchMyCompaniesUseCase)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(connectionsSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(connectionsSourceFactory!!.connectionsDataSource) { input -> input.networkState },
            Transformations.switchMap(connectionsSourceFactory!!.connectionsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() {
        connectionsSourceFactory?.connectionsDataSource?.value?.retry()
    }

    override fun refresh() {
        connectionsSourceFactory?.connectionsDataSource?.value?.invalidate()
    }

    override fun clear() {
        connectionsSourceFactory?.connectionsDataSource?.value?.dispose()
    }
}