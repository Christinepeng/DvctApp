package com.divercity.android.core.base.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.testing.OpenForTesting

/**
 * Created by lucas on 24/10/2018.
 */

@OpenForTesting
abstract class BaseViewModelPagination<T>
    (val repository: BaseDataSourceRepository<T>) : ViewModel() {

    var lastSearch: String? = null

    var subscribeToPaginatedLiveData = SingleLiveEvent<Unit>()

    fun pagedList(): LiveData<PagedList<T>> = repository.pagedList

    fun networkState(): LiveData<NetworkState> = repository.listing.networkState

    fun refreshState(): LiveData<NetworkState> = repository.listing.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    final fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        searchQuery?.let {
            if (it != lastSearch) {

                lastSearch = searchQuery
                repository.clear()

                repository.fetchData(it)

                lifecycleOwner?.let { lo ->
                    removeObservers(lo)
                    subscribeToPaginatedLiveData.call()
                }
            }
        }
    }

    fun fetchData() {
        repository.fetchData(null)
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        repository.pagedList.removeObservers(lifecycleOwner)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }
}