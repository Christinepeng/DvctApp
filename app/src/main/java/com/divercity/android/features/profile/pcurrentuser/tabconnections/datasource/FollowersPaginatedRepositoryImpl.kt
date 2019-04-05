package com.divercity.android.features.profile.pcurrentuser.tabconnections.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.pcurrentuser.tabconnections.usecase.FetchFollowersUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class FollowersPaginatedRepositoryImpl @Inject
internal constructor(private val fetchFollowersUseCase: FetchFollowersUseCase) {

    private lateinit var followersDataSourceFactory: FollowersDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    fun fetchData(userId : String): Listing<UserResponse> {

        val executor = Executors.newFixedThreadPool(5)

        fetchFollowersUseCase.userId = userId

        followersDataSourceFactory = FollowersDataSourceFactory(compositeDisposable, fetchFollowersUseCase)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(followersDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(followersDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(followersDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() = followersDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    fun refresh() = followersDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    fun clear() = compositeDisposable.dispose()
}
