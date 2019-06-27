package com.divercity.android.features.groups.groupdetail

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.Event
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.groupdetail.usecase.AcceptGroupAdminInviteUseCase
import com.divercity.android.features.groups.groupdetail.usecase.DeclineGroupAdminInviteUseCase
import com.divercity.android.features.groups.groupdetail.usecase.FetchGroupByIdUseCase
import com.divercity.android.features.groups.groupdetail.usecase.FetchGroupMembersUseCase
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class GroupDetailViewModel @Inject
constructor(
    private val fetchGroupMembersUseCase: FetchGroupMembersUseCase,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase,
    private val fetchGroupByIdUseCase: FetchGroupByIdUseCase,
    private val acceptGroupAdminInviteUseCase: AcceptGroupAdminInviteUseCase,
    private val declineGroupAdminInviteUseCase: DeclineGroupAdminInviteUseCase
) : BaseViewModel() {

    var fetchGroupMembersResponse = SingleLiveEvent<Resource<List<User>>>()
    var groupAdminInviteResponse = SingleLiveEvent<Resource<Unit>>()
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()
    var fetchGroupByIdResponse = MutableLiveData<Resource<GroupResponse>>()

    lateinit var groupId: String
    var groupLiveData = MutableLiveData<GroupResponse>()

    fun setGroup(group: GroupResponse) {
        groupId = group.id
        groupLiveData.postValue(group)
    }

    fun fetchGroupMembers(page: Int, size: Int, query: String?) {
        fetchGroupMembersResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<User>>() {
            override fun onFail(error: String) {
                fetchGroupMembersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupMembersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<User>) {
                fetchGroupMembersResponse.postValue(Resource.success(o))
            }
        }
        fetchGroupMembersUseCase.groupId = groupId
        fetchGroupMembersUseCase.execute(
            callback,
            Params(page, size, query)
        )
    }

    fun fetchGroup() {
        fetchGroupByIdResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<GroupResponse>() {
            override fun onFail(error: String) {
                fetchGroupByIdResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupByIdResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: GroupResponse) {
                fetchGroupByIdResponse.postValue(Resource.success(o))
                copyGroup(o)
                groupLiveData.postValue(o)
            }
        }
        fetchGroupByIdUseCase.execute(
            callback,
            FetchGroupByIdUseCase.Params.forGroups(groupId)
        )
    }

    fun joinGroup(group: GroupResponse) {
        joinGroupResponse.postValue(Event(Resource.loading(null)))

        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onSuccess(t: Unit) {
                onGroupJoinSuccess()
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
                onRequestToJoinGroupSuccess()
                requestToJoinResponse.postValue(Resource.success(o))
            }
        }
        requestToJoinUseCase.execute(
            callback,
            RequestJoinGroupUseCase.Params.toJoin(group.id)
        )
    }

    fun acceptGroupAdminInvite(inviteId: Int) {
        groupAdminInviteResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                groupAdminInviteResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                groupAdminInviteResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                onAcceptGroupAdminInvite()
                groupAdminInviteResponse.postValue(Resource.success(o))
            }
        }
        acceptGroupAdminInviteUseCase.execute(
            callback,
            AcceptGroupAdminInviteUseCase.Params(inviteId.toString())
        )
    }

    fun declineGroupAdminInvite(inviteId: Int) {
        groupAdminInviteResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                groupAdminInviteResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                groupAdminInviteResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                onDeclineGroupAdminInvite()
                groupAdminInviteResponse.postValue(Resource.success(o))
            }
        }
        declineGroupAdminInviteUseCase.execute(
            callback,
            DeclineGroupAdminInviteUseCase.Params(inviteId.toString())
        )
    }

    override fun onCleared() {
        super.onCleared()
        fetchGroupMembersUseCase.dispose()
        requestToJoinUseCase.dispose()
        joinGroupUseCase.dispose()
    }

    private fun onGroupJoinSuccess() {
        val group = groupLiveData.value
        group?.attributes?.isFollowedByCurrent = true
        groupLiveData.postValue(group)
    }

    private fun onAcceptGroupAdminInvite() {
        val group = groupLiveData.value
        group?.attributes?.groupAdminInvite = null
        group?.attributes?.isCurrentUserAdmin = true
        groupLiveData.postValue(group)
    }

    private fun onDeclineGroupAdminInvite() {
        val group = groupLiveData.value
        group?.attributes?.groupAdminInvite = null
        groupLiveData.postValue(group)
    }

    private fun onRequestToJoinGroupSuccess() {
        val group = groupLiveData.value
        group?.attributes?.requestToJoinStatus = "pending"
        groupLiveData.postValue(group)
    }

    fun getGroup(): GroupResponse? {
        return groupLiveData.value
    }

    private fun copyGroup(o: GroupResponse) {
        val group = groupLiveData.value
        if (group != null) {
            group.copy(o)
            groupLiveData.value = group
        } else {
            groupLiveData.value = o
        }
    }
}
