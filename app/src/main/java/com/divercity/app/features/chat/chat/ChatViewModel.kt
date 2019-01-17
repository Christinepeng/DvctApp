package com.divercity.app.features.chat.chat

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Handler
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.MySocket
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.app.data.entity.createchat.CreateChatResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.chat.chat.usecase.FetchMessagesUseCase
import com.divercity.app.features.chat.chat.usecase.FetchOrCreateChatUseCase
import com.divercity.app.features.chat.chat.usecase.SendMessagesUseCase
import com.divercity.app.repository.chat.ChatRepositoryImpl
import com.divercity.app.socket.ChatWebSocket
import com.google.gson.JsonElement
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
    private val chatWebSocket: ChatWebSocket
) : BaseViewModel() {

    var pageFetchList = ArrayList<Int>()

    var fetchCreateChatResponse = SingleLiveEvent<Resource<CreateChatResponse>>()
    var fetchMessagesResponse = SingleLiveEvent<Resource<List<ChatMessageResponse>>>()
    var sendMessageResponse = SingleLiveEvent<Resource<ChatMessageResponse>>()
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var pagedListLiveData: LiveData<PagedList<ChatMessageResponse>>? = null

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var handlerReconnect = Handler()
    var reconnectingAttempts = 0

    var chatId: Int? = null
    var userId: String? = null

    var hasFetchChatError = false

    companion object {
        const val RECONNECTING_ATTEMPTS = 4
        private const val PAGE_SIZE = 30
        private const val THRESHOLD = 10
    }

    fun start() {
        if (chatId != null && chatId != -1) {
            initializePagedList(chatId!!)
            connectToChatWebSocket(chatId!!)
            fetchMessages(userId!!, 0, PAGE_SIZE)
        } else if (userId != null) {
            getChatsIfExist(userId!!)
            fetchOrCreateChat(userId!!)
        }
    }

    fun getChatsIfExist(userId: String) {
        uiScope.launch {
            val user = "\"id\":\"".plus(userId).plus("\"")
            val chatId = chatMessageRepository.fetchChatIdByUser(user)
            if (chatId != -1 && chatId != 0)
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

                if (reconnectingAttempts != RECONNECTING_ATTEMPTS) {
                    Timber.d("fetchOrCreateChat: attemp: ".plus(reconnectingAttempts))
                    reconnectingAttempts++
                    handlerReconnect.postDelayed({
                        fetchOrCreateChat(otherUserId)
                    }, 3000)
                }
            }

            override fun onHttpException(error: JsonElement) {
                fetchCreateChatResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CreateChatResponse) {
                hasFetchChatError = false

                chatId = o.id.toInt()

                if (pagedListLiveData == null) {
                    initializePagedList(o.id.toInt())
                }

                uiScope.launch {
                    chatMessageRepository.insertChatOnDB(
                        ExistingUsersChatListItem(
                            chatId = o.id.toInt(),
                            chatUsers = o.attributes?.users
                        )
                    )
                    fetchMessages(otherUserId, 0, PAGE_SIZE)
                }

                fetchCreateChatResponse.postValue(Resource.success(o))

                connectToChatWebSocket(o.id.toInt())
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
                }
            }
        }
        compositeDisposable.add(callback)
        fetchMessagesUseCase.execute(
            callback, FetchMessagesUseCase.Params
                .forMsgs(
                    chatId.toString(),
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
                .forMsg(message, chatId.toString())
        )
    }

    fun insertChatDb(chatMessageResponse: ChatMessageResponse) {
        uiScope.launch {
            chatMessageRepository.insertChatMessageOnDB(chatMessageResponse)
        }
    }

    fun checkIfFetchMoreData(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int){
        val items = firstVisibleItemPosition + visibleItemCount + THRESHOLD

        if (items % PAGE_SIZE == 0) {
            val page = items / PAGE_SIZE
            if (!pageFetchList.contains(page)) {
                pageFetchList.add(page)
                fetchMessages(userId!!, page, PAGE_SIZE)
            }
        } else if (visibleItemCount + firstVisibleItemPosition == totalItemCount) {
//            We add one to check if the next page has been fetched
            val page = totalItemCount / PAGE_SIZE + 1
            if (!pageFetchList.contains(page)) {
                pageFetchList.add(page)
                fetchMessages(userId!!, page, PAGE_SIZE)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun connectToChatWebSocket(chatId : Int) {
        chatWebSocket.addOnChatMessageReceivedListener(object :
            ChatWebSocket.OnChatMessageReceived {
            override fun onChatMessageReceived(chat: ChatMessageResponse) {
                insertChatDb(chat)
            }
        })

        chatWebSocket.connect(chatId.toString())
    }

    fun checkIfReconnectionIsNeeded() {
        if (chatId != null && chatId != -1 && chatWebSocket.getSocketState() == MySocket.State.CONNECT_ERROR) {
            chatWebSocket.stopTryingToReconnect()
            connectToChatWebSocket(chatId!!)
        }
    }

    fun checkErrorsToReconnect(){
        if (hasFetchChatError) {
            fetchOrCreateChat(userId!!)
        } else {
            checkIfReconnectionIsNeeded()
        }
    }

    fun onDestroy(){
        chatWebSocket.close()
    }
}