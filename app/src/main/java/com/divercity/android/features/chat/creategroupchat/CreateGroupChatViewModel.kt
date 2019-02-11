package com.divercity.android.features.chat.creategroupchat

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.chat.creategroupchatbody.CreateGroupChatBody
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.chat.creategroupchat.usecase.AddChatGroupMemberUseCase
import com.divercity.android.features.chat.creategroupchat.usecase.CreateGroupChatUseCase
import com.google.gson.JsonElement
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CreateGroupChatViewModel @Inject
constructor(
    private val createGroupChatUseCase: CreateGroupChatUseCase,
    private val addChatGroupMemberUseCase: AddChatGroupMemberUseCase
) : BaseViewModel() {

    val createGroupChatResponse = SingleLiveEvent<Resource<CreateChatResponse>>()
    val addChatGroupMemberResponse = SingleLiveEvent<Resource<CreateChatResponse>>()

    var photoFile: File? = null
    lateinit var listMembers: ArrayList<UserResponse>

    fun createGroup(members: HashSet<UserResponse>, groupName: String) {
        createGroupChatResponse.postValue(Resource.loading(null))
        listMembers = ArrayList(members)
        val callback = object : DisposableObserverWrapper<CreateChatResponse>() {

            override fun onFail(error: String) {
                createGroupChatResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement?) {
                createGroupChatResponse.postValue(Resource.error(error.toString(), null))

            }

            override fun onSuccess(t: CreateChatResponse) {
                createGroupChatResponse.postValue(Resource.success(t))
                listMembers.removeAt(0)
                addChatGroupMembers(t, listMembers)
            }
        }
        createGroupChatUseCase.execute(
            callback, CreateGroupChatUseCase.Params.forUser(
                listMembers[0].id,
                CreateGroupChatBody(
                    groupName,
                    picture = if (photoFile == null) null else ImageUtils.getStringBase64(
                        photoFile!!,
                        600,
                        600
                    )
                )
            )
        )
    }

    private fun addChatGroupMembers(chatResponse: CreateChatResponse, members : ArrayList<UserResponse>) {
        val membersIds = ArrayList<String>()
        for (i in members)
            membersIds.add(i.id)

        addChatGroupMemberResponse.value = Resource.loading(null)
        val callback = object : DisposableObserverWrapper<Boolean>() {

            override fun onFail(error: String) {
                addChatGroupMemberResponse.value = Resource.error(error, null)
            }

            override fun onHttpException(error: JsonElement?) {
                addChatGroupMemberResponse.value = Resource.error(error.toString(), null)
            }

            override fun onSuccess(t: Boolean) {
                addChatGroupMemberResponse.value = Resource.success(chatResponse)
            }
        }
        addChatGroupMemberUseCase.execute(
            callback,
            AddChatGroupMemberUseCase.Params.forUser(chatResponse.id, membersIds)
        )
    }

    override fun onCleared() {
        super.onCleared()
        addChatGroupMemberUseCase.dispose()
        createGroupChatUseCase.dispose()
    }
}
