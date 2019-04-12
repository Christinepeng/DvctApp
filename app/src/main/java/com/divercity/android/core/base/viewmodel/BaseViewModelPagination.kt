package com.divercity.android.core.base.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent

/**
 * Created by lucas on 24/10/2018.
 */

abstract class BaseViewModelPagination<T>
    (val repository: BaseDataSourceRepository<T>) : ViewModel() {

    var lastSearch: String? = null
    private lateinit var listing: Listing<T>
    lateinit var pagedList: LiveData<PagedList<T>>

    var subscribeToPaginatedLiveData = SingleLiveEvent<Unit>()

    fun networkState(): LiveData<NetworkState> = listing.networkState

    fun refreshState(): LiveData<NetworkState> = listing.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        searchQuery?.let {
            if (it != lastSearch) {

                lastSearch = searchQuery
                repository.clear()

                listing = repository.fetchData(it)
                pagedList = listing.pagedList

                lifecycleOwner?.let { lo ->
                    removeObservers(lo)
                    subscribeToPaginatedLiveData.call()
                }
            }
        }
    }

    fun fetchData() {
        listing = repository.fetchData(null)
        pagedList = listing.pagedList
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedList.removeObservers(lifecycleOwner)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }
}