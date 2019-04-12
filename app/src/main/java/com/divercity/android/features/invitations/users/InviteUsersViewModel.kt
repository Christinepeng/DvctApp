package com.divercity.android.features.invitations.users

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.invitation.GroupInviteResponse
import com.divercity.android.data.entity.group.invitation.user.GroupInviteUser
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.invitations.users.usecase.InviteUsersToGroupUseCase
import com.divercity.android.repository.paginated.UsersByCharacterPaginatedRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class InviteUsersViewModel @Inject
constructor(repository: UsersByCharacterPaginatedRepository,
            private val inviteUsersToGroupUseCase: InviteUsersToGroupUseCase)
    : BaseViewModelPagination<Any>(repository) {

    var inviteUsersResponse = SingleLiveEvent<Resource<String>>()

    init {
        fetchData(null, "")
    }

    fun inviteToGroup(groupId: String, contacts: List<String>) {
        inviteUsersResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<GroupInviteResponse>() {
            override fun onFail(error: String) {
                inviteUsersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                inviteUsersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: GroupInviteResponse) {
                inviteUsersResponse.postValue(Resource.success("Group invitations sent successfully"))
            }
        }

        inviteUsersToGroupUseCase.execute(callback,
                InviteUsersToGroupUseCase.Params.forGroups(
                        GroupInviteUser(
                                groupId = groupId,
                                users = contacts
                        )
                ))
    }

    override fun onCleared() {
        super.onCleared()
        inviteUsersToGroupUseCase.dispose()
    }
}
