package com.divercity.android.features.profile.userconnections

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.usecase.ConnectUserUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionsViewModel @Inject
constructor(
    repository: UserConnectionsPaginatedRepository,
    private val sessionRepository: SessionRepository,
    private val connectUserUseCase: ConnectUserUseCase
) : BaseViewModelPagination<User>(repository) {

    var connectUserResponse = MutableLiveData<Resource<ConnectUserResponse>>()

    private var userId: String? = null

    fun fetchConnections(userId : String) {
        if(this.userId == null) {
            this.userId = userId
            (repository as UserConnectionsPaginatedRepository).setUserId(userId)
            fetchData()
        }
    }

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
}