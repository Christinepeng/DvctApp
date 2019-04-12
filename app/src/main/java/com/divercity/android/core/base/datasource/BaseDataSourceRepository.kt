package com.divercity.android.core.base.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.utils.Listing
import java.util.concurrent.Executors

abstract class BaseDataSourceRepository<T> {

    var dataSourceFactory: BaseDataSourceFactory<T>? = null

    companion object {

        const val pageSize = 20
    }

    abstract fun getUseCase() : UseCase<List<T>, Params>

    fun fetchData(query: String?): Listing<T> {
        val executor = Executors.newFixedThreadPool(5)

        dataSourceFactory = BaseDataSourceFactory(getUseCase(), query)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(dataSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(dataSourceFactory!!.dataSource) { input -> input.networkState },
            Transformations.switchMap(dataSourceFactory!!.dataSource) { input -> input.initialLoad }
        )
    }

    fun retry() {
        dataSourceFactory?.dataSource?.value?.retry()
    }

    fun refresh() {
        dataSourceFactory?.dataSource?.value?.invalidate()
    }

    fun clear() {
        dataSourceFactory?.dataSource?.value?.dispose()
    }
}
