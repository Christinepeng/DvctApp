package com.divercity.android.features.company.companyadminadd

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.features.chat.newchat.datasource.UserPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */
 
class CompanyAdminAddViewModel @Inject
constructor(private val repository : UserPaginatedRepositoryImpl): BaseViewModel(){

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedUserList: LiveData<PagedList<Any>>
    private lateinit var listingPaginatedJob: Listing<Any>
    private lateinit var lastSearch: String

    init {
        fetchUsers(null, null)
    }

    fun fetchUsers(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        if (searchQuery == null) {
            lastSearch = ""
            fetchData(lifecycleOwner, lastSearch)
        } else if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner, lastSearch)
        }
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String) {
        repository.clear()

        listingPaginatedJob = repository.fetchData(searchQuery)
        pagedUserList = listingPaginatedJob.pagedList

        lifecycleOwner?.let {
            removeObservers(it)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState.removeObservers(lifecycleOwner)
        refreshState.removeObservers(lifecycleOwner)
        pagedUserList.removeObservers(lifecycleOwner)
    }


    val networkState: LiveData<NetworkState>
        get() = listingPaginatedJob.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}
