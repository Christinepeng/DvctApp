package com.divercity.android.features.profile.otheruser

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.otheruser.usecase.AcceptConnectionRequestUseCase
import com.divercity.android.features.profile.otheruser.usecase.DeclineConnectionRequestUseCase
import com.divercity.android.features.profile.usecase.FetchUserDataUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class OtherUserProfileViewModel @Inject
constructor(
    private val fetchUserDataUseCase: FetchUserDataUseCase,
    private val acceptConnectionRequestUseCase: AcceptConnectionRequestUseCase,
    private val declineConnectionRequestUseCase: DeclineConnectionRequestUseCase
) : BaseViewModel() {

    var user = MutableLiveData<UserResponse>()

    var userId: String? = null

    var fetchUserDataResponse = MutableLiveData<Resource<UserResponse>>()
    var acceptDeclineConnectionRequestResponse = MutableLiveData<Resource<ConnectUserResponse>>()

    fun setUser(user : UserResponse){
        userId = user.id
        this.user.value = user
    }

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
                user.postValue(o)
                fetchUserDataResponse.postValue(Resource.success(o))
            }
        }
        fetchUserDataUseCase.execute(
            callback,
            FetchUserDataUseCase.Params.forUserData(userId)
        )
    }

    fun acceptConnectionRequest(userId : String) {
        acceptDeclineConnectionRequestResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ConnectUserResponse>() {
            override fun onFail(error: String) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ConnectUserResponse) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.success(o))
            }
        }
        acceptConnectionRequestUseCase.execute(
            callback,
            AcceptConnectionRequestUseCase.Params.toAccept(userId)
        )
    }

    fun declineConnectionRequest(userId : String) {
        acceptDeclineConnectionRequestResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                acceptDeclineConnectionRequestResponse.postValue(Resource.success(ConnectUserResponse()))
            }
        }
        declineConnectionRequestUseCase.execute(
            callback,
            DeclineConnectionRequestUseCase.Params.toDecline(userId)
        )
    }

    override fun onCleared() {
        super.onCleared()
        fetchUserDataUseCase.dispose()
        acceptConnectionRequestUseCase.dispose()
        declineConnectionRequestUseCase.dispose()
    }
}
