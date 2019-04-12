package com.divercity.android.features.profile.pcurrentuser.tabconnections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.pcurrentuser.tabconnections.datasource.FollowersPaginatedRepositoryImpl
import com.divercity.android.features.profile.usecase.ConnectUserUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionsViewModel @Inject
constructor(
    private val repository: FollowersPaginatedRepositoryImpl,
    private val sessionRepository: SessionRepository,
    private val connectUserUseCase: ConnectUserUseCase
) : BaseViewModel() {

    lateinit var pagedListConnections: LiveData<PagedList<UserResponse>>
    private var listingConnections: Listing<UserResponse>? = null
    var connectUserResponse = MutableLiveData<Resource<ConnectUserResponse>>()
    var userId: String? = null

    fun isCurrentUser(): Boolean {
        return sessionRepository.getUserId() == userId
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

    fun fetchFollowers(userId: String) {
        // To call it once
        this.userId = userId
        if (listingConnections == null) {
            listingConnections = repository.fetchData(userId)
            pagedListConnections = listingConnections!!.pagedList
        }
    }

    fun networkState(): LiveData<NetworkState> = listingConnections!!.networkState

    fun refreshState(): LiveData<NetworkState> = listingConnections!!.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}