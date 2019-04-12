package com.divercity.android.features.groups.createeditgroup.step3

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.creategroup.GroupOfInterest
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.createeditgroup.step3.usecase.CreateGroupUseCase
import com.divercity.android.features.groups.createeditgroup.step3.usecase.EditGroupUseCase
import com.google.gson.JsonElement
import java.io.IOException
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class CreateEditGroupStep3ViewModel @Inject
constructor(
    private val createGroupUseCase: CreateGroupUseCase,
    private val editGroupUseCase: EditGroupUseCase
) : BaseViewModel() {

    var createEditGroupResponse = SingleLiveEvent<Resource<GroupResponse>>()

    fun createGroup(
        groupName: String,
        description: String,
        isPrivate: Boolean,
        photoPath: String?
    ) {
        try {
            var photoBase64: String? = null
            if (photoPath != null && photoPath != "")
                photoBase64 = ImageUtils.getStringBase64(photoPath, 800, 800)

            createEditGroupResponse.postValue(Resource.loading<GroupResponse>(null))
            val callback = object : DisposableObserverWrapper<GroupResponse>() {
                override fun onFail(error: String) {
                    createEditGroupResponse.postValue(Resource.error(error, null))
                }

                override fun onHttpException(error: JsonElement) {
                    createEditGroupResponse.postValue(Resource.error(error.toString(), null))
                }

                override fun onSuccess(o: GroupResponse) {
                    createEditGroupResponse.postValue(Resource.success(o))
                }
            }
            val groupType = if (isPrivate) "private" else "public"
            createGroupUseCase.execute(
                callback,
                CreateGroupUseCase.Params.forGroups(
                    GroupOfInterest(
                        title = groupName,
                        description = description,
                        groupType = groupType,
                        picture = photoBase64
                    )
                )
            )

        } catch (exception: IOException) {
            createEditGroupResponse.postValue(Resource.error(exception.message!!, null))
        }
    }

    fun editGroup(
        groupResponse: GroupResponse,
        groupName: String?,
        description: String?,
        isPrivate: Boolean,
        photoPath: String?
    ) {
        try {
            var photoBase64: String? = null
            if (photoPath != null && photoPath != "")
                photoBase64 = ImageUtils.getStringBase64(photoPath, 800, 800)

            createEditGroupResponse.postValue(Resource.loading<GroupResponse>(null))
            val callback = object : DisposableObserverWrapper<GroupResponse>() {
                override fun onFail(error: String) {
                    createEditGroupResponse.postValue(Resource.error(error, null))
                }

                override fun onHttpException(error: JsonElement) {
                    createEditGroupResponse.postValue(Resource.error(error.toString(), null))
                }

                override fun onSuccess(o: GroupResponse) {
                    createEditGroupResponse.postValue(Resource.success(o))
                }
            }
            val groupType = if (isPrivate) "private" else "public"
            editGroupUseCase.execute(
                callback,
                EditGroupUseCase.Params.forGroups(
                    groupResponse.id,
                    GroupOfInterest(
                        title = groupName,
                        description = description,
                        groupType = groupType,
                        picture = photoBase64
                    )
                )
            )

        } catch (exception: IOException) {
            this.createEditGroupResponse.postValue(Resource.error(exception.message!!, null))
        }
    }

    override fun onCleared() {
        super.onCleared()
        createGroupUseCase.dispose()
        editGroupUseCase.dispose()
    }
}
