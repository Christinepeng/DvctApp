package com.divercity.android.features.user.allconnections.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.PaginatedQueryRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.features.chat.usecase.FetchUsersUseCase
import com.divercity.android.features.user.allconnections.usecase.FetchConnectionsUseCase
import com.divercity.android.model.user.User
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class ConnectionsPaginatedRepositoryImpl
@Inject internal constructor(
    private val fetchConnectionsUseCase: FetchConnectionsUseCase,
    private val fetchUsersUseCase: FetchUsersUseCase
) : PaginatedQueryRepository<User> {

    private var connectionsSourceFactory: ConnectionsSourceFactory? = null

    companion object {

        private val pageSize = 20
    }

    override fun fetchData(query: String?): Listing<User> {

        val executor = Executors.newFixedThreadPool(5)

        connectionsSourceFactory =
            ConnectionsSourceFactory(
                fetchUsersUseCase,
                fetchConnectionsUseCase,
                query!!
            )

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