package com.divercity.app.features.groups.trending.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.app.core.base.PaginatedQueryRepository
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.features.groups.trending.usecase.FetchTrendingGroupsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class TrendingGroupsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchGroupsUseCase: FetchTrendingGroupsUseCase) : PaginatedQueryRepository<GroupResponse> {

    private lateinit var trendingGroupsDataSourceFactory: TrendingGroupsDataSourceFactory
    val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    override fun fetchData(query : String?): Listing<GroupResponse> {

        val executor = Executors.newFixedThreadPool(5)

        trendingGroupsDataSourceFactory = TrendingGroupsDataSourceFactory(compositeDisposable, fetchGroupsUseCase, query)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(trendingGroupsDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(trendingGroupsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(trendingGroupsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() = trendingGroupsDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    override fun refresh() = trendingGroupsDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    override fun clear() = compositeDisposable.dispose()
}
