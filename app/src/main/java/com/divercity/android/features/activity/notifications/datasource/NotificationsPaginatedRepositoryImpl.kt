package com.divercity.android.features.activity.notifications.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.features.activity.notifications.usecase.FetchNotificationsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class NotificationsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchNotificationsUseCase: FetchNotificationsUseCase) {

    private lateinit var notificationsDataSourceFactory: NotificationsDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    fun fetchData(): Listing<NotificationResponse> {

        val executor = Executors.newFixedThreadPool(5)

        notificationsDataSourceFactory =
            NotificationsDataSourceFactory(
                compositeDisposable,
                fetchNotificationsUseCase
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
