package com.divercity.android.core.base.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.utils.Listing
import com.divercity.android.testing.OpenForTesting
import java.util.concurrent.Executors

@OpenForTesting
abstract class BaseDataSourceRepository<T> {

    private var dataSourceFactory: BaseDataSourceFactory<T>? = null
    lateinit var listing: Listing<T>
    lateinit var pagedList: LiveData<PagedList<T>>
    final var pageSize = 20

    abstract fun getUseCase(): UseCase<List<T>, Params>

    fun fetchData(query: String?) {
        val executor = Executors.newFixedThreadPool(5)

        dataSourceFactory = BaseDataSourceFactory(getUseCase(), query)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        pagedList = LivePagedListBuilder(dataSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        listing = Listing(
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
