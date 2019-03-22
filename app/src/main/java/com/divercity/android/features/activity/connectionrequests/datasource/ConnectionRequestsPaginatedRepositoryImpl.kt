package com.divercity.android.features.activity.connectionrequests.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.PaginatedRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.features.activity.connectionrequests.usecase.FetchConnectionRequestsUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class ConnectionRequestsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchFollowedGroupsUseCase: FetchConnectionRequestsUseCase) :
    PaginatedRepository<ConnectionItem> {

    private lateinit var connectionRequestsDataSourceFactory: ConnectionRequestsDataSourceFactory

    companion object {

        private val pageSize = 20
    }

    override fun fetchData(): Listing<ConnectionItem> {

        val executor = Executors.newFixedThreadPool(5)

        connectionRequestsDataSourceFactory =
            ConnectionRequestsDataSourceFactory(
                fetchFollowedGroupsUseCase
            )

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(connectionRequestsDataSourceFactory, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(connectionRequestsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
            Transformations.switchMap(connectionRequestsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() {
        connectionRequestsDataSourceFactory.groupsInterestsDataSource.value?.retry()
    }

    override fun refresh() {
        connectionRequestsDataSourceFactory.groupsInterestsDataSource.value?.invalidate()
    }

    override fun clear() {
        connectionRequestsDataSourceFactory.groupsInterestsDataSource.value?.dispose()
    }
}