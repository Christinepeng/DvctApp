package com.divercity.android.features.activity.notifications

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.activity.notifications.datasource.NotificationsPaginatedRepositoryImpl
import com.divercity.android.features.activity.notifications.model.NotificationPositionModel
import com.divercity.android.features.activity.notifications.usecase.MarkNotificationReadUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class NotificationsViewModel @Inject
constructor(private val repository: NotificationsPaginatedRepositoryImpl,
            private val markNotificationReadUseCase: MarkNotificationReadUseCase) : BaseViewModel() {

    lateinit var pagedApplicantsList: LiveData<PagedList<NotificationResponse>>
    private lateinit var listingPaginatedJob: Listing<NotificationResponse>
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var markNotificationReadResponse = SingleLiveEvent<Resource<NotificationPositionModel>>()

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

    fun markNotificationRead(notification: NotificationPositionModel) {
        markNotificationReadResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                markNotificationReadResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                markNotificationReadResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                notification.notificationResponse.attributes?.read = true
                markNotificationReadResponse.postValue(Resource.success(notification))
            }
        }
        markNotificationReadUseCase.execute(
            callback,
            MarkNotificationReadUseCase.Params.toMark(notification.notificationResponse.id!!))
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

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }
}