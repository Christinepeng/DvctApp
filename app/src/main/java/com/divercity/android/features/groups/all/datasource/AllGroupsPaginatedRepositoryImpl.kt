package com.divercity.android.features.groups.all.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.PaginatedQueryRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.all.usecase.FetchAllGroupsUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class AllGroupsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchGroupsUseCase: FetchAllGroupsUseCase) :
    PaginatedQueryRepository<GroupResponse> {

    private var allGroupsDataSourceFactory: AllGroupsDataSourceFactory? = null

    companion object {

        const val pageSize = 20
    }

    override fun fetchData(query: String?): Listing<GroupResponse> {

        val executor = Executors.newFixedThreadPool(5)

        allGroupsDataSourceFactory = AllGroupsDataSourceFactory(fetchGroupsUseCase, query)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(allGroupsDataSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(allGroupsDataSourceFactory!!.groupsInterestsDataSource) { input -> input.networkState },
            Transformations.switchMap(allGroupsDataSourceFactory!!.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() {
        allGroupsDataSourceFactory?.groupsInterestsDataSource?.value?.retry()
    }


    override fun refresh() {
        allGroupsDataSourceFactory?.groupsInterestsDataSource?.value?.invalidate()
    }


    override fun clear() {
        allGroupsDataSourceFactory?.groupsInterestsDataSource?.value?.dispose()
    }
}
