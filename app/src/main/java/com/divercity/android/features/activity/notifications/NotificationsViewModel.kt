package com.divercity.android.features.activity.notifications

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.features.activity.notifications.datasource.NotificationsPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class NotificationsViewModel @Inject
constructor(private val repository: NotificationsPaginatedRepositoryImpl) : BaseViewModel() {

    lateinit var pagedApplicantsList: LiveData<PagedList<NotificationResponse>>
    private lateinit var listingPaginatedJob: Listing<NotificationResponse>
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()

    init {
        fetchData(null)
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?) {
        listingPaginatedJob = repository.fetchData()
        pagedApplicantsList = listingPaginatedJob.pagedList

        lifecycleOwner?.let {
            removeObservers(it)
        }

        subscribeToPaginatedLiveData.call()
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedApplicantsList.removeObservers(lifecycleOwner)
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}