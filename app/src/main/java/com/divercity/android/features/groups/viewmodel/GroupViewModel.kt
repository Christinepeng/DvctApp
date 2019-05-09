package com.divercity.android.features.groups.viewmodel

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.model.position.GroupPositionModel
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class GroupViewModel @Inject
constructor(
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase
) : BaseViewModel() {

    var requestToJoinPrivateGroupResponse = SingleLiveEvent<Resource<GroupPositionModel>>()
    var joinPublicGroupResponse = SingleLiveEvent<Resource<GroupPositionModel>>()

    fun joinGroup(group: GroupPositionModel) {
        joinPublicGroupResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onSuccess(t: Boolean) {
                joinPublicGroupResponse.postValue(Resource.success(group))
            }

            override fun onFail(error: String) {
                joinPublicGroupResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                joinPublicGroupResponse.postValue(Resource.error(error.toString(), null))
            }
        }
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group.group.id))
    }

    fun requestToJoinGroup(group: GroupPositionModel) {
        requestToJoinPrivateGroupResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<MessageResponse>() {
            override fun onFail(error: String) {
                requestToJoinPrivateGroupResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                requestToJoinPrivateGroupResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: MessageResponse) {
                requestToJoinPrivateGroupResponse.postValue(Resource.success(group))
            }
        }
        requestToJoinUseCase.execute(
            callback,
            RequestJoinGroupUseCase.Params.toJoin(group.group.id)
        )
    }

    override fun onCleared() {
        super.onCleared()
        requestToJoinUseCase.dispose()
        joinGroupUseCase.dispose()
    }
}
