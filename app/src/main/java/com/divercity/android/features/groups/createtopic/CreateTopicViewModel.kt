package com.divercity.android.features.groups.createtopic

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.createtopic.usecase.CreateTopicUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class CreateTopicViewModel @Inject
constructor(
    private val createTopicUseCase : CreateTopicUseCase
) : BaseViewModel() {

    var createTopicResponse = SingleLiveEvent<Resource<Any>>()

    fun createNewTopic(question : String, group: GroupResponse, image : String?) {
        createTopicResponse.postValue(Resource.loading<Boolean>(null))
        val callback = object : DisposableObserverWrapper<QuestionResponse>() {
            override fun onFail(error: String) {
                createTopicResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                createTopicResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: QuestionResponse) {
                createTopicResponse.postValue(Resource.success(o))
            }
        }
        createTopicUseCase.execute(callback, CreateTopicUseCase.Params.forTopic(question, group.id, image))
    }

    override fun onCleared() {
        super.onCleared()
        createTopicUseCase.dispose()
    }
}
