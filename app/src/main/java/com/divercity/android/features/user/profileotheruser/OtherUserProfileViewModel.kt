package com.divercity.android.features.user.profileotheruser

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.user.profileotheruser.usecase.AcceptConnectionRequestUseCase
import com.divercity.android.features.user.profileotheruser.usecase.DeclineConnectionRequestUseCase
import com.divercity.android.features.user.usecase.ConnectUserUseCase
import com.divercity.android.features.user.usecase.FetchUserDataUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class OtherUserProfileViewModel @Inject
constructor(
    private val fetchUserDataUseCase: FetchUserDataUseCase,
    private val acceptConnectionRequestUseCase: AcceptConnectionRequestUseCase,
    private val declineConnectionRequestUseCase: DeclineConnectionRequestUseCase,
    private val connectUserUseCase: ConnectUserUseCase
) : BaseViewModel() {

    var userLiveData = MutableLiveData<User>()

    var userId: String? = null

    var fetchUserDataResponse = MutableLiveData<Resource<User>>()
    var acceptDeclineConnectionRequestResponse = MutableLiveData<Resource<ConnectUserResponse>>()
    var connectUserResponse = MutableLiveData<Resource<ConnectUserResponse>>()

    fun setUser(user: User) {
        userId = user.id
        this.userLiveData.value = user
    }

    fun fetchUserData() {
        fetchUserDataResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                fetchUserDataResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchUserDataResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: User) {
                copyUser(o)
                fetchUserDataResponse.postValue(Resource.success(o))
            }
        }
        fetchUserDataUseCase.execute(
            callback,
            FetchUserDataUseCase.Params.forUserData(userId)
        )
    }

    fun acceptConnectionRequest(userId: String) {
        acceptDeclineConnectionRequestResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ConnectUserResponse>() {
            override fun onFail(error: String) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineConnectionRequestResponse.postValue(
                    Resource.error(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: ConnectUserResponse) {
                onAcceptConnectionRequest()
                acceptDeclineConnectionRequestResponse.postValue(Resource.success(o))
            }
        }
        acceptConnectionRequestUseCase.execute(
            callback,
            AcceptConnectionRequestUseCase.Params.toAccept(userId)
        )
    }

    fun declineConnectionRequest(userId: String) {
        acceptDeclineConnectionRequestResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineConnectionRequestResponse.postValue(
                    Resource.error(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: Unit) {
                onDeclineConnectionRequest()
                acceptDeclineConnectionRequestResponse.postValue(
                    Resource.success(
                        ConnectUserResponse()
                    )
                )
            }
        }
        declineConnectionRequestUseCase.execute(
            callback,
            DeclineConnectionRequestUseCase.Params.toDecline(userId)
        )
    }

    fun connectToUser() {
        connectUserResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ConnectUserResponse>() {
            override fun onFail(error: String) {
                connectUserResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                connectUserResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ConnectUserResponse) {
                onConnectUser(o)
                connectUserResponse.postValue(Resource.success(o))
            }
        }
        connectUserUseCase.execute(
            callback,
            ConnectUserUseCase.Params.toFollow(userId!!)
        )
    }

    override fun onCleared() {
        super.onCleared()
        fetchUserDataUseCase.dispose()
        acceptConnectionRequestUseCase.dispose()
        declineConnectionRequestUseCase.dispose()
    }

    private fun onAcceptConnectionRequest() {
        val user = userLiveData.value
        user?.connected = "connected"
        userLiveData.value = user
    }

    private fun onDeclineConnectionRequest() {
        val user = userLiveData.value
        user?.connected = "not_connected"
        userLiveData.value = user
    }

    private fun onConnectUser(o: ConnectUserResponse) {
        val user = userLiveData.value
        user?.connected = o.attributes?.status
        userLiveData.value = user
    }

    private fun copyUser(o : User){
        val user = userLiveData.value
        if(user != null) {
            user.copy(o)
            userLiveData.value = user
        } else {
            userLiveData.value = o
        }
    }
}
