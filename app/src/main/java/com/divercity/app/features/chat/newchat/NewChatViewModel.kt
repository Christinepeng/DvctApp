package com.divercity.app.features.chat.newchat

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.features.chat.newchat.datasource.UserPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */
 
class NewChatViewModel @Inject
constructor(private val repository : UserPaginatedRepositoryImpl): BaseViewModel(){

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedUserList: LiveData<PagedList<Any>>
    private lateinit var listingPaginatedJob: Listing<Any>
    private var lastSearch: String? = null

    init {
        fetchUsers(null, "")
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchUsers(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        searchQuery?.let {
            if (it != lastSearch) {
                repository.compositeDisposable.clear()
                lastSearch = it
                listingPaginatedJob = repository.fetchData(searchQuery)
                pagedUserList = listingPaginatedJob.pagedList

                lifecycleOwner?.let { lifecycleOwner ->
                    removeObservers(lifecycleOwner)
                    subscribeToPaginatedLiveData.call()
                }
            }
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedUserList.removeObservers(lifecycleOwner)
    }
}