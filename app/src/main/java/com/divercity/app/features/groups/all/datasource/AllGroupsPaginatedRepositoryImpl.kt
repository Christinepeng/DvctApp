package com.divercity.app.features.groups.all.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.app.core.base.PaginatedQueryRepository
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.features.onboarding.selectgroups.usecase.FetchGroupsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class AllGroupsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchGroupsUseCase: FetchGroupsUseCase) : PaginatedQueryRepository<GroupResponse> {

    private lateinit var allGroupsDataSourceFactory: AllGroupsDataSourceFactory
    val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    override fun fetchData(query : String?): Listing<GroupResponse> {

        val executor = Executors.newFixedThreadPool(5)

        allGroupsDataSourceFactory = AllGroupsDataSourceFactory(compositeDisposable, fetchGroupsUseCase, query)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(allGroupsDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(allGroupsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(allGroupsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() = allGroupsDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    override fun refresh() = allGroupsDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    override fun clear() = compositeDisposable.dispose()
}
