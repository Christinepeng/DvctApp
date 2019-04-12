package com.divercity.android.features.invitations.contacts

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.invitation.GroupInviteResponse
import com.divercity.android.data.entity.group.invitation.contact.GroupInviteContact
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.invitations.contacts.usecase.InviteContactToGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class InvitePhoneContactsViewModel @Inject
constructor(private val inviteContactToGroupUseCase: InviteContactToGroupUseCase) :
    BaseViewModel() {

    var inviteContactsResponse = SingleLiveEvent<Resource<String>>()

    fun inviteToGroup(groupId: String, contacts : List<String>) {
        inviteContactsResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<GroupInviteResponse>() {
            override fun onFail(error: String) {
                inviteContactsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                inviteContactsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: GroupInviteResponse) {
                inviteContactsResponse.postValue(Resource.success("Group invitations sent successfully"))
            }
        }

        inviteContactToGroupUseCase.execute(callback,
            InviteContactToGroupUseCase.Params.forGroups(
                GroupInviteContact(groupId = groupId,
                    phoneNumbers = contacts)
            ))
    }

    override fun onCleared() {
        super.onCleared()
        inviteContactToGroupUseCase.dispose()
    }
}