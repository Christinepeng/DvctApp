package com.divercity.android.features.jobs.jobposting.sharetogroup.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.PaginatedQueryRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.jobs.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class FollowedGroupsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchFollowedGroupsUseCase: FetchFollowedGroupsUseCase) :
    PaginatedQueryRepository<GroupResponse> {

    private var followedGroupsSourceFactory: FollowedGroupsSourceFactory? = null

    companion object {

        private val pageSize = 20
    }

    override fun fetchData(query: String?): Listing<GroupResponse> {

        val executor = Executors.newFixedThreadPool(5)

        followedGroupsSourceFactory = FollowedGroupsSourceFactory(fetchFollowedGroupsUseCase, query)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(followedGroupsSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(followedGroupsSourceFactory!!.groupsInterestsDataSource) { input -> input.networkState },
            Transformations.switchMap(followedGroupsSourceFactory!!.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() {
        followedGroupsSourceFactory?.groupsInterestsDataSource?.value?.retry()
    }

    override fun refresh() {
        followedGroupsSourceFactory?.groupsInterestsDataSource?.value?.invalidate()
    }

    override fun clear() {
        followedGroupsSourceFactory?.groupsInterestsDataSource?.value?.dispose()
    }
}