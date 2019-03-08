package com.divercity.android.features.profile.tabconnections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.tabconnections.datasource.FollowersPaginatedRepositoryImpl
import com.divercity.android.features.profile.usecase.ConnectUserUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionsViewModel @Inject
constructor(private val repository: FollowersPaginatedRepositoryImpl,
            private val sessionRepository: SessionRepository,
            private val connectUserUseCase: ConnectUserUseCase) : BaseViewModel() {

    lateinit var pagedApplicantsList: LiveData<PagedList<UserResponse>>
    private lateinit var listingPaginatedJob: Listing<UserResponse>
    var connectUserResponse = MutableLiveData<Resource<ConnectUserResponse>>()
    lateinit var currentUserId : String

    init {
        fetchFollowers()
    }

    fun connectToUser(userId : String) {
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

    private fun fetchFollowers() {
        listingPaginatedJob = repository.fetchData(sessionRepository.getUserId())
        pagedApplicantsList = listingPaginatedJob.pagedList
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}