package com.divercity.app.features.chat.chat

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Handler
import android.util.Log
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.MySocket
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.app.data.entity.createchat.CreateChatResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.db.chat.Chat
import com.divercity.app.features.chat.chat.usecase.FetchMessagesUseCase
import com.divercity.app.features.chat.chat.usecase.FetchOrCreateChatUseCase
import com.divercity.app.features.chat.chat.usecase.SendMessagesUseCase
import com.divercity.app.repository.chat.ChatRepositoryImpl
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class ChatViewModel @Inject
constructor(
        private val fetchOrCreateChatUseCase: FetchOrCreateChatUseCase,
        private val chatMessageRepository: ChatRepositoryImpl,
        private val fetchMessagesUseCase: FetchMessagesUseCase,
        private val sendMessagesUseCase: SendMessagesUseCase,
        private val socket: MySocket) : BaseViewModel() {

    var pageFetchList = ArrayList<Int>()

    var fetchCreateChatResponse = SingleLiveEvent<Resource<CreateChatResponse>>()
    var fetchMessagesResponse = SingleLiveEvent<Resource<List<ChatMessageResponse>>>()
    var sendMessageResponse = SingleLiveEvent<Resource<ChatMessageResponse>>()
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var createChatResponse: CreateChatResponse? = null

    var handlerReconnect = Handler()

    val RECONNECTING_ATTEMPS = 4
    var reconnectingAttemps = 0

    var hasFetchChatError = false

//    val pagedListLiveData: LiveData<PagedList<ChatMessageResponse>> by lazy {
//        val dataSourceFactory = chatMessageRepository.getMessagesByChatId()
//        val config = PagedList.Config.Builder()
//            .setPageSize(15)
//            .setInitialLoadSizeHint(30)
//            .setPrefetchDistance(10)
//            .build()
//        LivePagedListBuilder(dataSourceFactory, config).build()
//    }

    var pagedListLiveData: LiveData<PagedList<ChatMessageResponse>>? = null

    fun getChatsIfExist(otherUserId: String) {
        uiScope.launch {
            val chatId = chatMessageRepository.getChatIdByOtherUserIdFromDB(otherUserId.toInt())
            if (chatId != 0)
                initializePagedList(chatId)
        }
    }

    fun initializePagedList(chatId: Int) {
        val dataSourceFactory = chatMessageRepository.getMessagesByChatId(chatId)
        val config = PagedList.Config.Builder()
                .setPageSize(15)
                .setInitialLoadSizeHint(30)
                .setPrefetchDistance(10)
                .build()
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory, config).build()
        subscribeToPaginatedLiveData.call()
    }

    fun fetchOrCreateChat(otherUserId: String) {
        handlerReconnect.removeCallbacksAndMessages(null)
        fetchCreateChatResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<CreateChatResponse>() {
            override fun onFail(error: String) {
                hasFetchChatError = true
                fetchCreateChatResponse.postValue(Resource.error(error, null))

                if (reconnectingAttemps != RECONNECTING_ATTEMPS) {
                    Timber.d("fetchOrCreateChat: attemp: ".plus(reconnectingAttemps))
                    reconnectingAttemps++
                    handlerReconnect.postDelayed({
                        fetchOrCreateChat(otherUserId)
                    }, 3000)
                }
            }

            override fun onHttpException(error: JsonElement) {
                hasFetchChatError = true
                fetchCreateChatResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CreateChatResponse) {
                hasFetchChatError = false
                createChatResponse = o
                if (pagedListLiveData == null) {
                    initializePagedList(o.id!!.toInt())
                }
                uiScope.launch {
                    chatMessageRepository.insertChatOnDB(Chat(o.id!!.toInt(), otherUserId.toInt()))
                }
                fetchCreateChatResponse.postValue(Resource.success(o))
                connectWebSocket()

                uiScope.launch {
                    val rows = chatMessageRepository.countMessagesByChatIdFromDB(createChatResponse?.id!!.toInt())
                    fetchMessages(otherUserId, 0, 30)
                }
            }
        }
        compositeDisposable.add(callback)
        fetchOrCreateChatUseCase.execute(
                callback,
                FetchOrCreateChatUseCase.Params.forUser(otherUserId)
        )
    }

    fun fetchMessages(otherUserId: String, page: Int, size: Int) {
        fetchMessagesResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<DataChatMessageResponse>() {
            override fun onFail(error: String) {
                pageFetchList.remove(page)
                fetchMessagesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchMessagesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: DataChatMessageResponse) {
                fetchMessagesResponse.postValue(Resource.success(o.data?.chats!!))

                uiScope.launch {
                    chatMessageRepository.insertChatMessagesOnDB(o.data.chats)
//                    val rows = chatMessageRepository.countMessagesByChatIdFromDB(createChatResponse?.id!!.toInt())
//                    if (rows < o.meta!!.totalCount!!) {
//                        fetchMessages(otherUserId, 0, o.meta.totalCount!! - rows + 15)
//                    }
                }
            }
        }
        compositeDisposable.add(callback)
        fetchMessagesUseCase.execute(
                callback, FetchMessagesUseCase.Params
                .forMsgs(
                        createChatResponse?.id!!,
                        otherUserId,
                        page,
                        size,
                        null
                )
        )
    }

    fun sendMessage(message: String) {
        sendMessageResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ChatMessageResponse>() {
            override fun onFail(error: String) {
                sendMessageResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                sendMessageResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ChatMessageResponse) {
                sendMessageResponse.postValue(Resource.success(null))
            }
        }
        compositeDisposable.add(callback)
        sendMessagesUseCase.execute(
                callback, SendMessagesUseCase.Params
                .forMsg(message, createChatResponse?.id!!)
        )
    }

    fun insertChatDb(chatMessageResponse: ChatMessageResponse) {
        uiScope.launch {
            chatMessageRepository.insertChatMessageOnDB(chatMessageResponse)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun connectWebSocket() {
//        val identifier =
//                "\"{\\\"chat_id\\\": \\\"" + createChatResponse?.id + "\\\",\\\"channel\\\":\\\"MessagesChannel\\\"}\"}"
//        socket.sendOnOpen("subscribe", identifier)
        socket.setMessageListener(object : MySocket.OnMessageListener() {

            override fun onMessage(data: String?) {

                try {
                    // Parse message text
                    val jsonParser = JsonParser()
                    val response = jsonParser.parse(data)
                    val message = response.asJsonObject.getAsJsonObject("message")

                    val gson = Gson()
                    if (message != null) {
                        val chat = gson.fromJson(message, ChatMessageResponse::class.java)
                        insertChatDb(chat)
                    }

                } catch (e: Exception) {
                    // Message text not in JSON format or don't have {event}|{data} object
                    Log.e("WebSocket", "Unknown message format.")
                }
            }

        })

        socket.onEvent(MySocket.EVENT_OPEN, object : MySocket.OnEventListener() {

            override fun onMessage(event: String?) {
                val identifier =
                        "\"{\\\"chat_id\\\": \\\"" + createChatResponse?.id + "\\\",\\\"channel\\\":\\\"MessagesChannel\\\"}\"}"
                Timber.d("onOpen: ".plus(identifier))
                socket.send("subscribe", identifier)
            }
        })
        socket.connect()
    }

    fun checkIfReconnectionIsNeeded() {
        if (createChatResponse != null && socket.state == MySocket.State.CONNECT_ERROR) {
            socket.stopTryingToReconnect()
            connectWebSocket()
        }
    }

    fun closeSocket() {
        socket.close()
    }
}