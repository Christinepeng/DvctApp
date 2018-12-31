package com.divercity.app.features.chat.chat

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.createchat.CreateChatResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.chat.chat.usecase.FetchOrCreateChatUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */
 
class ChatViewModel @Inject
constructor(private val fetchOrCreateChatUseCase: FetchOrCreateChatUseCase): BaseViewModel(){

    var fetchCreateChatResponse = SingleLiveEvent<Resource<CreateChatResponse>>()

    fun fetchOrCreateChat(otherUserId: String) {
        fetchCreateChatResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<CreateChatResponse>() {
            override fun onFail(error: String) {
                fetchCreateChatResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchCreateChatResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CreateChatResponse) {
                fetchCreateChatResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchOrCreateChatUseCase.execute(callback, FetchOrCreateChatUseCase.Params.forUser(otherUserId))
    }
}
