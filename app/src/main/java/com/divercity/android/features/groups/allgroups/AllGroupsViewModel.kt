package com.divercity.android.features.groups.allgroups

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.divercity.android.model.position.GroupPosition
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class AllGroupsViewModel @Inject
constructor(
    repository: AllGroupsPaginatedRepository,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase
) : BaseViewModelPagination<GroupResponse>(repository) {

    var requestToJoinPrivateGroupResponse = SingleLiveEvent<Resource<GroupPosition>>()
    var joinPublicGroupResponse = SingleLiveEvent<Resource<GroupPosition>>()

    init {
        fetchData(null, "")
    }

    fun joinGroup(group: GroupPosition) {
        joinPublicGroupResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onSuccess(t: Unit) {
                joinPublicGroupResponse.postValue(Resource.success(group))
            }

            override fun onFail(error: String) {
                joinPublicGroupResponse.postValue(Resource.error(error, group))
            }

            override fun onHttpException(error: JsonElement) {
                joinPublicGroupResponse.postValue(Resource.error(error.toString(), group))
            }
        }
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group.group.id))
    }

    fun requestToJoinGroup(group: GroupPosition) {
        requestToJoinPrivateGroupResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<MessageResponse>() {
            override fun onFail(error: String) {
                requestToJoinPrivateGroupResponse.postValue(Resource.error(error, group))
            }

            override fun onHttpException(error: JsonElement) {
                requestToJoinPrivateGroupResponse.postValue(Resource.error(error.toString(), group))
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
