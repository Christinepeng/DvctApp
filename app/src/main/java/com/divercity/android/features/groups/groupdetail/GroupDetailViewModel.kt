package com.divercity.android.features.groups.groupdetail

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.Event
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.groupdetail.usecase.FetchGroupByIdUseCase
import com.divercity.android.features.groups.groupdetail.usecase.FetchGroupMembersUseCase
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class GroupDetailViewModel @Inject
constructor(
        private val fetchGroupMembersUseCase: FetchGroupMembersUseCase,
        private val joinGroupUseCase: JoinGroupUseCase,
        private val requestToJoinUseCase : RequestJoinGroupUseCase,
        private val fetchGroupByIdUseCase: FetchGroupByIdUseCase
) : BaseViewModel() {

    var fetchGroupMembersResponse = SingleLiveEvent<Resource<List<UserResponse>>>()
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()
    var fetchGroupByIdResponse = MutableLiveData<Resource<GroupResponse>>()

    var group = MutableLiveData<GroupResponse>()

    fun fetchGroupMembers(groupId: String, page : Int, size: Int, query : String?) {
        fetchGroupMembersResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<UserResponse>>() {
            override fun onFail(error: String) {
                fetchGroupMembersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupMembersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<UserResponse>) {
                fetchGroupMembersResponse.postValue(Resource.success(o))
            }
        }
        fetchGroupMembersUseCase.execute(callback,
                FetchGroupMembersUseCase.Params.forGroups(groupId, page, size, query))
    }

    fun fetchGroupById(groupId: String) {
        fetchGroupByIdResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<GroupResponse>() {
            override fun onFail(error: String) {
                fetchGroupByIdResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupByIdResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: GroupResponse) {
                group.postValue(o)
                fetchGroupByIdResponse.postValue(Resource.success(o))
            }
        }
        fetchGroupByIdUseCase.execute(callback,
            FetchGroupByIdUseCase.Params.forGroups(groupId))
    }

    fun joinGroup(group: GroupResponse) {
        joinGroupResponse.postValue(Event(Resource.loading(null)))

        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onSuccess(t: Boolean) {
                joinGroupResponse.postValue(Event(Resource.success(t)))
            }

            override fun onFail(error: String) {
                joinGroupResponse.postValue(Event(Resource.error(error, null)))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Event(Resource.error(error.toString(), null)))
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
        requestToJoinUseCase.execute(callback,
                RequestJoinGroupUseCase.Params.toJoin(group.id))
    }

    override fun onCleared() {
        super.onCleared()
        fetchGroupMembersUseCase.dispose()
        requestToJoinUseCase.dispose()
        joinGroupUseCase.dispose()
    }
}
