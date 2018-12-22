package com.divercity.app.features.profile.tabgroups.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.app.core.base.PaginatedQueryRepository
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.features.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class FollowingGroupsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchFollowedGroupsUseCase: FetchFollowedGroupsUseCase) :
        PaginatedQueryRepository<GroupResponse> {

    private lateinit var followingGroupsDataSourceFactory: FollowingGroupsDataSourceFactory
    val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    override fun fetchData(query: String?): Listing<GroupResponse> {

        val executor = Executors.newFixedThreadPool(5)

        followingGroupsDataSourceFactory =
                FollowingGroupsDataSourceFactory(
                        compositeDisposable,
                        fetchFollowedGroupsUseCase,
                        query
                )

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(followingGroupsDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(followingGroupsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(followingGroupsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() =
            followingGroupsDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    override fun refresh() =
            followingGroupsDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    override fun clear() = compositeDisposable.dispose()
}
