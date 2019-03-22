package com.divercity.android.features.activity.connectionrequests

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.features.activity.connectionrequests.datasource.ConnectionRequestsPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionRequestsViewModel @Inject
constructor(private val repository: ConnectionRequestsPaginatedRepositoryImpl) : BaseViewModel() {

    lateinit var pagedConnectionList: LiveData<PagedList<ConnectionItem>>
    private lateinit var listingConnections: Listing<ConnectionItem>

    init {
        fetchConnectionRequests()
    }

    private fun fetchConnectionRequests() {
        listingConnections = repository.fetchData()
        pagedConnectionList = listingConnections.pagedList
    }

    fun networkState(): LiveData<NetworkState> = listingConnections.networkState

    fun refreshState(): LiveData<NetworkState> = listingConnections.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}