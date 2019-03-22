package com.divercity.android.features.home.people.connections

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.home.people.connections.datasource.ConnectionsPaginatedRepositoryImpl
import com.divercity.android.features.profile.usecase.ConnectUserUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class AllConnectionsViewModel @Inject
constructor(
    private val repository: ConnectionsPaginatedRepositoryImpl,
    private val connectUserUseCase: ConnectUserUseCase
) : BaseViewModel() {

    lateinit var pagedUserList: LiveData<PagedList<UserResponse>>
    lateinit var listingPaginatedUser: Listing<UserResponse>

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var connectUserResponse = MutableLiveData<Resource<ConnectUserResponse>>()

    internal var jobId: String? = null

    private lateinit var lastSearch : String

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedUser.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedUser.refreshState

    init {
        fetchConnections(null, null)
    }

    fun connectToUser(userId: String) {
        connectUserResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ConnectUserResponse>() {
            override fun onFail(error: String) {
                connectUserResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                connectUserResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ConnectUserResponse) {
                connectUserResponse.postValue(Resource.success(o))
            }
        }
        connectUserUseCase.execute(
            callback,
            ConnectUserUseCase.Params.toFollow(userId)
        )
    }

    fun fetchConnections(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
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

        listingPaginatedUser = repository.fetchData(searchQuery)
        pagedUserList = listingPaginatedUser.pagedList

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

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }

    fun retry() {
        repository.retry()
    }

    fun refresh() {
        repository.refresh()
    }
}
