package com.divercity.android.features.profile.potheruser

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.potheruser.usecase.AcceptConnectionRequestUseCase
import com.divercity.android.features.profile.potheruser.usecase.DeclineConnectionRequestUseCase
import com.divercity.android.features.profile.usecase.FetchUserDataUseCase
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
    private val declineConnectionRequestUseCase: DeclineConnectionRequestUseCase
) : BaseViewModel() {

    var user = MutableLiveData<User>()

    var userId: String? = null

    var fetchUserDataResponse = MutableLiveData<Resource<User>>()
    var acceptDeclineConnectionRequestResponse = MutableLiveData<Resource<ConnectUserResponse>>()

    fun setUser(user : User){
        userId = user.id
        this.user.value = user
    }

    fun fetchProfileData() {
        fetchUserDataResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                fetchUserDataResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchUserDataResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: User) {
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
