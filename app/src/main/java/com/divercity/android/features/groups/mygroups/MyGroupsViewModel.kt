package com.divercity.android.features.groups.mygroups

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class MyGroupsViewModel @Inject
constructor(
    repository: MyGroupsPaginatedRepository,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase
) : BaseViewModelPagination<GroupResponse>(repository) {

    var joinGroupResponse = SingleLiveEvent<Resource<Any>>()
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()

    init {
        fetchData()
    }

    fun joinGroup(group: GroupResponse) {
        joinGroupResponse.postValue(Resource.loading<Boolean>(null))
        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onFail(error: String) {
                joinGroupResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Boolean) {
                joinGroupResponse.postValue(Resource.success(o))
            }
        }
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group.id))
    }

    fun requestToJoinGroup(group: GroupResponse) {
        requestToJoinResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<MessageResponse>() {
            override fun onFail(error: String) {
                requestToJoinResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                requestToJoinResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: MessageResponse) {
                requestToJoinResponse.postValue(Resource.success(o))
            }
        }
        requestToJoinUseCase.execute(
            callback,
            RequestJoinGroupUseCase.Params.toJoin(group.id)
        )
    }

    override fun onCleared() {
        super.onCleared()
        requestToJoinUseCase.dispose()
        joinGroupUseCase.dispose()
    }
}
