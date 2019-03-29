package com.divercity.android.features.company.selectcompany.base.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.PaginatedQueryRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.selectcompany.base.usecase.FetchCompaniesUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class CompanyPaginatedRepositoryImpl @Inject
internal constructor(private val fetchCompaniesUseCase: FetchCompaniesUseCase) :
    PaginatedQueryRepository<CompanyResponse> {

    private var companyDataSourceFactory: CompanyDataSourceFactory? = null

    companion object {

        private val pageSize = 20
    }

    override fun fetchData(query: String?): Listing<CompanyResponse> {

        val executor = Executors.newFixedThreadPool(5)

        companyDataSourceFactory = CompanyDataSourceFactory(fetchCompaniesUseCase, query)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(companyDataSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(companyDataSourceFactory!!.groupsInterestsDataSource) { input -> input.networkState },
            Transformations.switchMap(companyDataSourceFactory!!.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() {
        companyDataSourceFactory?.groupsInterestsDataSource?.value?.retry()
    }

    override fun refresh() {
        companyDataSourceFactory?.groupsInterestsDataSource?.value?.invalidate()
    }

    override fun clear() {
        companyDataSourceFactory?.groupsInterestsDataSource?.value?.dispose()
    }
}
