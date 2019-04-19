package com.divercity.android.features.groups.deletegroupadmin

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.deletegroupadmin.usecase.DeleteGroupAdminUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteGroupAdminViewModel @Inject
constructor(
    repository: GroupAdminsPaginatedRepository,
    private val deleteGroupAdminUseCase: DeleteGroupAdminUseCase
) : BaseViewModelPagination<UserResponse>(repository) {

    var deleteGroupAdminResponse = SingleLiveEvent<Resource<Unit>>()

    var groupId: String = ""
        set(value) {
            (repository as GroupAdminsPaginatedRepository).setGroupId(value)
        }

    fun deleteGroupAdmin(groupId: String, userIds: List<String>) {
        deleteGroupAdminResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                deleteGroupAdminResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                deleteGroupAdminResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                deleteGroupAdminResponse.postValue(Resource.success(o))
            }
        }
        deleteGroupAdminUseCase.execute(
            callback,
            DeleteGroupAdminUseCase.Params(groupId, userIds)
        )
    }
}
