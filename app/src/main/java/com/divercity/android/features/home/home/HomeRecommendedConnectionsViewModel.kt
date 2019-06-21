package com.divercity.android.features.home.home

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.home.home.usecase.DiscardRecommendedUserUseCase
import com.divercity.android.features.user.usecase.ConnectUserUseCase
import com.divercity.android.model.position.UserPosition
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

class HomeRecommendedConnectionsViewModel @Inject
constructor(
    repository: RecommendedConnectionsPaginatedRepository,
    private val connectUserUseCase: ConnectUserUseCase,
    private val discardRecommendedUserUseCase: DiscardRecommendedUserUseCase
) : BaseViewModelPagination<User>(repository) {

    var connectUserResponse = SingleLiveEvent<Resource<UserPosition>>()
    var discardRecommendedUserResponse = SingleLiveEvent<Resource<UserPosition>>()

    init {
        fetchData()
    }

    fun connectToUser(userPosition: UserPosition) {
        connectUserResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ConnectUserResponse>() {
            override fun onFail(error: String) {
                connectUserResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                connectUserResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ConnectUserResponse) {
                userPosition.user.connected = o.attributes?.status
                connectUserResponse.postValue(Resource.success(userPosition))
            }
        }
        connectUserUseCase.execute(
            callback,
            ConnectUserUseCase.Params.toFollow(userPosition.user.id)
        )
    }

    fun discardRecommendedConnection(userPosition: UserPosition) {
        discardRecommendedUserResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                discardRecommendedUserResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                discardRecommendedUserResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                discardRecommendedUserResponse.postValue(Resource.success(userPosition))
            }
        }
        discardRecommendedUserUseCase.execute(
            callback,
            DiscardRecommendedUserUseCase.Params.toDiscard(listOf(userPosition.user.id))
        )
    }
}
