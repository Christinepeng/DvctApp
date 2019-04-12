package com.divercity.android.features.activity.connectionrequests

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.R
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.activity.connectionrequests.datasource.ConnectionRequestsPaginatedRepositoryImpl
import com.divercity.android.features.activity.connectionrequests.model.GroupInvitationNotificationPosition
import com.divercity.android.features.activity.connectionrequests.model.JoinGroupRequestPosition
import com.divercity.android.features.activity.connectionrequests.model.UserPosition
import com.divercity.android.features.activity.connectionrequests.usecase.AcceptGroupInviteUseCase
import com.divercity.android.features.activity.connectionrequests.usecase.AcceptJoinGroupRequestUseCase
import com.divercity.android.features.activity.connectionrequests.usecase.DeclineGroupInviteUseCase
import com.divercity.android.features.activity.connectionrequests.usecase.DeclineJoinGroupRequestUseCase
import com.divercity.android.features.profile.potheruser.usecase.AcceptConnectionRequestUseCase
import com.divercity.android.features.profile.potheruser.usecase.DeclineConnectionRequestUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionRequestsViewModel @Inject
constructor(
    private val context: Context,
    private val repository: ConnectionRequestsPaginatedRepositoryImpl,
    private val acceptConnectionRequestUseCase: AcceptConnectionRequestUseCase,
    private val declineConnectionRequestUseCase: DeclineConnectionRequestUseCase,
    private val acceptGroupInviteUseCase: AcceptGroupInviteUseCase,
    private val declineGroupInviteUseCase: DeclineGroupInviteUseCase,
    private val acceptJoinGroupRequestUseCase: AcceptJoinGroupRequestUseCase,
    private val declineJoinGroupRequestUseCase: DeclineJoinGroupRequestUseCase
) : BaseViewModel() {

    lateinit var pagedConnectionList: LiveData<PagedList<ConnectionItem>>
    private lateinit var listingConnections: Listing<ConnectionItem>

    var acceptDeclineConnectionRequestResponse = SingleLiveEvent<Resource<UserPosition>>()
    var acceptDeclineGroupInviteResponse = SingleLiveEvent<Resource<GroupInvitationNotificationPosition>>()
    var acceptDeclineJoinGroupRequest = SingleLiveEvent<Resource<JoinGroupRequestPosition>>()

    init {
        fetchConnectionRequests()
    }

    private fun fetchConnectionRequests() {
        listingConnections = repository.fetchData()
        pagedConnectionList = listingConnections.pagedList
    }

    fun networkState(): LiveData<NetworkState> = listingConnections.networkState

    fun refreshState(): LiveData<NetworkState> = listingConnections.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun acceptConnectionRequest(user: UserPosition) {
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
                user.user.userAttributes?.connected = o.attributes?.status
                acceptDeclineConnectionRequestResponse.postValue(Resource.success(user))
            }
        }
        acceptConnectionRequestUseCase.execute(
            callback,
            AcceptConnectionRequestUseCase.Params.toAccept(user.user.id)
        )
    }

    fun declineConnectionRequest(user: UserPosition) {
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
                user.user.userAttributes?.connected = "declined"
                acceptDeclineConnectionRequestResponse.postValue(Resource.success(user))
            }
        }
        declineConnectionRequestUseCase.execute(
            callback,
            DeclineConnectionRequestUseCase.Params.toDecline(user.user.id)
        )
    }

    fun acceptGroupInvitation(invite: GroupInvitationNotificationPosition) {
        acceptDeclineGroupInviteResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                acceptDeclineGroupInviteResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineGroupInviteResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                invite.groupInvitation.attributes?.status = context.getString(R.string.accepted)
                acceptDeclineGroupInviteResponse.postValue(Resource.success(invite))
            }
        }
        acceptGroupInviteUseCase.execute(
            callback,
            AcceptGroupInviteUseCase.Params.toAccept(invite.groupInvitation.id!!)
        )
    }

    fun declineGroupInvitation(invite: GroupInvitationNotificationPosition) {
        acceptDeclineGroupInviteResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                acceptDeclineGroupInviteResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineGroupInviteResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                invite.groupInvitation.attributes?.status = context.getString(R.string.declined)
                acceptDeclineGroupInviteResponse.postValue(Resource.success(invite))
            }
        }
        declineGroupInviteUseCase.execute(
            callback,
            DeclineGroupInviteUseCase.Params.toDecline(invite.groupInvitation.id!!)
        )
    }

    fun acceptJoinGroupRequest(request: JoinGroupRequestPosition) {
        acceptDeclineJoinGroupRequest.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                acceptDeclineJoinGroupRequest.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineJoinGroupRequest.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                request.joinGroupRequest.attributes?.state = context.getString(R.string.accepted)
                acceptDeclineJoinGroupRequest.postValue(Resource.success(request))
            }
        }
        acceptJoinGroupRequestUseCase.execute(
            callback,
            AcceptJoinGroupRequestUseCase.Params.toAccept(
                request.joinGroupRequest.attributes?.groupInfo?.id!!,
                request.joinGroupRequest.attributes?.userId!!)
        )
    }

    fun declineJoinGroupRequest(request: JoinGroupRequestPosition) {
        acceptDeclineJoinGroupRequest.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                acceptDeclineJoinGroupRequest.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                acceptDeclineJoinGroupRequest.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                request.joinGroupRequest.attributes?.state = context.getString(R.string.declined)
                acceptDeclineJoinGroupRequest.postValue(Resource.success(request))
            }
        }
        declineJoinGroupRequestUseCase.execute(
            callback,
            DeclineJoinGroupRequestUseCase.Params.toDecline(
                request.joinGroupRequest.attributes?.groupInfo?.id!!,
                request.joinGroupRequest.attributes?.userId!!)
        )
    }

    override fun onCleared() {
        super.onCleared()
        acceptConnectionRequestUseCase.dispose()
        declineConnectionRequestUseCase.dispose()
    }
}