package com.divercity.android.features.profile.tabfollowing.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.tabfollowing.usecase.FetchFollowingUseCase
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepository
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class FollowingPaginatedRepositoryImpl @Inject
internal constructor(private val fetchFollowersUseCase: FetchFollowingUseCase,
                     private val sessionRepository: SessionRepository) {

    private lateinit var followingDataSourceFactory: FollowingDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    fun fetchData(): Listing<UserResponse> {

        val executor = Executors.newFixedThreadPool(5)

        fetchFollowersUseCase.userId = sessionRepository.getUserId()

        followingDataSourceFactory =
                FollowingDataSourceFactory(
                    compositeDisposable,
                    fetchFollowersUseCase
                )

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(followingDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(followingDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(followingDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() = followingDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    fun refresh() = followingDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    fun clear() = compositeDisposable.dispose()
}
