package com.divercity.android.features.groups.createeditgroup.step1

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.createeditgroup.step1.usecase.DeleteGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class CreateEditGroupStep1ViewModel @Inject
constructor(
    private val deleteGroupUseCase: DeleteGroupUseCase
) : BaseViewModel() {

    var deleteGroupResponse = SingleLiveEvent<Resource<Any>>()

    fun deleteGroup(group: GroupResponse) {
        deleteGroupResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onFail(error: String) {
                deleteGroupResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                deleteGroupResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Boolean) {
                deleteGroupResponse.postValue(Resource.success(o))
            }
        }
        deleteGroupUseCase.execute(
            callback,
            DeleteGroupUseCase.Params.forGroups(group.id)
        )
    }

    override fun onCleared() {
        super.onCleared()
        deleteGroupUseCase.dispose()
    }
}
