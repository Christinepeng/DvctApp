package com.divercity.android.features.activity.connectionrequests.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.features.activity.connectionrequests.usecase.FetchConnectionRequestsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class ConnectionRequestsPaginatedRepositoryImpl @Inject
internal constructor(
    private val fetchConnectionRequestsUseCase: FetchConnectionRequestsUseCase) {

    private lateinit var notificationsDataSourceFactory: ConnectionRequestsDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    fun fetchData(): Listing<ConnectionItem> {

        val executor = Executors.newFixedThreadPool(5)

        notificationsDataSourceFactory =
            ConnectionRequestsDataSourceFactory(
                compositeDisposable,
                fetchConnectionRequestsUseCase
            )

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(notificationsDataSourceFactory, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(notificationsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
            Transformations.switchMap(notificationsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() = notificationsDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    fun refresh() = notificationsDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    fun clear() = compositeDisposable.dispose()
}
