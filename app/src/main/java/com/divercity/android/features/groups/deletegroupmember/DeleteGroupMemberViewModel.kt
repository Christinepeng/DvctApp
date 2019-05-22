package com.divercity.android.features.groups.deletegroupmember

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.message.MessagesResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.deletegroupmember.usecase.DeleteGroupMemberUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteGroupMemberViewModel @Inject
constructor(
    repository: GroupMembersPaginatedRepository,
    private val deleteGroupMemberUseCase: DeleteGroupMemberUseCase
) : BaseViewModelPagination<User>(repository) {

    var deleteGroupMemberResponse = SingleLiveEvent<Resource<String>>()

    var groupId: String = ""
        set(value) {
            (repository as GroupMembersPaginatedRepository).setGroupId(value)
            field = value
        }

    fun deleteGroupMember(userIds: List<String>) {
        deleteGroupMemberResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<MessagesResponse>() {
            override fun onFail(error: String) {
                deleteGroupMemberResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                deleteGroupMemberResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: MessagesResponse) {
                if(o.hasErrors == true)
                    deleteGroupMemberResponse.postValue(Resource.success(o.messages.toString()))
                else
                    deleteGroupMemberResponse.postValue(Resource.success("Members removed successfully"))
            }
        }
        deleteGroupMemberUseCase.execute(
            callback,
            DeleteGroupMemberUseCase.Params(groupId, userIds)
        )
    }
}
