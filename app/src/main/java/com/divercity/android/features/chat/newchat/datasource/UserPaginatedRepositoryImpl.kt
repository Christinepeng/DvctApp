package com.divercity.android.features.chat.newchat.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.PaginatedQueryRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.features.chat.usecase.FetchUsersUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class UserPaginatedRepositoryImpl @Inject
internal constructor(private val fetchUsersUseCase: FetchUsersUseCase) : PaginatedQueryRepository<Any> {

    private var userDataSourceFactory: UserDataSourceFactory? = null

    companion object {

        const val pageSize = 20
    }

    override fun fetchData(query: String?): Listing<Any> {

        val executor = Executors.newFixedThreadPool(5)

        userDataSourceFactory = UserDataSourceFactory(fetchUsersUseCase, query)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(userDataSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(userDataSourceFactory!!.groupsInterestsDataSource) { input -> input.networkState },
            Transformations.switchMap(userDataSourceFactory!!.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() {
        userDataSourceFactory?.groupsInterestsDataSource?.value?.retry()
    }

    override fun refresh() {
        userDataSourceFactory?.groupsInterestsDataSource?.value?.invalidate()
    }

    override fun clear() {
        userDataSourceFactory?.groupsInterestsDataSource?.value?.dispose()
    }
}
