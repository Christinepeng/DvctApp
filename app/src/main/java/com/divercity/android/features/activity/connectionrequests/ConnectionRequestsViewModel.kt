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

    var pagedApplicantsList: LiveData<PagedList<ConnectionItem>> ?= null
    private lateinit var listingPaginatedJob: Listing<ConnectionItem>

    init {
        fetchConnectionRequests()
    }

    private fun fetchConnectionRequests() {
        listingPaginatedJob = repository.fetchData()
        pagedApplicantsList = listingPaginatedJob.pagedList
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}