package com.divercity.android.features.profile.pcurrentuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.pcurrentuser.tabconnections.datasource.FollowersPaginatedRepositoryImpl
import com.divercity.android.features.profile.usecase.FetchUserDataUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class CurrentUserProfileViewModel @Inject
constructor(
    private val fetchUserDataUseCase: FetchUserDataUseCase,
    private val sessionRepository: SessionRepository,
    private val connectionsRepository: FollowersPaginatedRepositoryImpl
    ) : BaseViewModel() {

    init {
        fetchFollowers()
    }

    // ConnectionsFragment
    lateinit var pagedListConnections: LiveData<PagedList<UserResponse>>
    private lateinit var listingConnections: Listing<UserResponse>

    private fun fetchFollowers() {
        listingConnections = connectionsRepository.fetchData(sessionRepository.getUserId())
        pagedListConnections = listingConnections.pagedList
    }

    fun networkState(): LiveData<NetworkState> = listingConnections.networkState

    fun refreshState(): LiveData<NetworkState> = listingConnections.refreshState

    fun retry() = connectionsRepository.retry()

    fun refresh() = connectionsRepository.refresh()

    // CurrentUserProfileFragment
    var fetchUserDataResponse = MutableLiveData<Resource<UserResponse>>()

    fun fetchProfileData() {
        fetchUserDataResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<UserResponse>() {
            override fun onFail(error: String) {
                fetchUserDataResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchUserDataResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: UserResponse) {
                sessionRepository.saveUserData(o)
                fetchUserDataResponse.postValue(Resource.success(o))
            }
        }
        fetchUserDataUseCase.execute(
            callback,
            FetchUserDataUseCase.Params.forUserData(sessionRepository.getUserId())
        )
    }

    fun getCurrentUser() : LiveData<UserResponse> {
        return sessionRepository.getUserDB()
    }

    fun getCurrentUserId() : String {
        return sessionRepository.getUserId()
    }

    override fun onCleared() {
        super.onCleared()
        fetchUserDataUseCase.dispose()
    }
}