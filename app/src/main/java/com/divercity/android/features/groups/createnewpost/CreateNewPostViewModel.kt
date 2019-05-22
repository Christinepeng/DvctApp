package com.divercity.android.features.groups.createnewpost

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.createnewpost.usecase.CreateNewPostUseCase
import com.divercity.android.features.groups.selectfollowedgroup.FollowedGroupsPaginatedRepository
import com.divercity.android.model.Question
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class CreateNewPostViewModel @Inject
constructor(
    repository: FollowedGroupsPaginatedRepository,
    private val createPostUseCase: CreateNewPostUseCase
) : BaseViewModelPagination<GroupResponse>(repository) {

    init {
        fetchData()
    }

    var createTopicResponse = SingleLiveEvent<Resource<Unit>>()

    fun createNewPosts(question: String, groupIds: List<String>, image: String?) {
        createTopicResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Question>() {
            override fun onFail(error: String) {
                createTopicResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                createTopicResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Question) {
                createTopicResponse.postValue(Resource.success(null))
            }
        }
        createPostUseCase.execute(callback, CreateNewPostUseCase.Params(question, groupIds, image))
    }

    override fun onCleared() {
        super.onCleared()
        createPostUseCase.dispose()
    }
}
