package com.divercity.android.features.chat.chat

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.MySocket
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.android.data.entity.chat.messages.Meta
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.chat.chat.usecase.FetchChatMembersUseCase
import com.divercity.android.features.chat.chat.usecase.FetchMessagesUseCase
import com.divercity.android.features.chat.chat.usecase.FetchOrCreateChatUseCase
import com.divercity.android.features.chat.chat.usecase.SendMessagesUseCase
import com.divercity.android.model.user.User
import com.divercity.android.model.usermentionable.QueryTokenUserMentionable
import com.divercity.android.model.usermentionable.UserMentionable
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.socket.ChatWebSocket
import com.google.gson.JsonElement
import com.linkedin.android.spyglass.tokenization.QueryToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class ChatViewModel @Inject
constructor(
    private val fetchOrCreateChatUseCase: FetchOrCreateChatUseCase,
    private val chatMessageRepository: ChatRepository,
    private val fetchMessagesUseCase: FetchMessagesUseCase,
    private val sendMessagesUseCase: SendMessagesUseCase,
    private val chatWebSocket: ChatWebSocket,
    private val fetchChatMembersUseCase: FetchChatMembersUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    var pageFetchList = ArrayList<Int>()
    var chatMembers: List<UserMentionable>? = null

    var fetchCreateChatResponse = SingleLiveEvent<Resource<CreateChatResponse>>()
    var fetchMessagesResponse = SingleLiveEvent<Resource<DataChatMessageResponse>>()
    var fetchChatMembersResponse = SingleLiveEvent<Resource<QueryTokenUserMentionable>>()
    var sendMessageResponse = SingleLiveEvent<Resource<ChatMessageEntityResponse>>()
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var pagedListLiveData: LiveData<PagedList<ChatMessageEntityResponse>>? = null

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var handlerReconnect = Handler()
    var reconnectingAttempts = 0

    var chatId: Int? = null
    var userId: String? = null

    var hasFetchChatError = false
    var hasFetchGroupMembersError = false

    var mentions = HashSet<User>()

    companion object {
        const val RECONNECTING_ATTEMPTS = 4
        private const val PAGE_SIZE = 30
        private const val THRESHOLD = 10
    }

    fun start() {
        if (chatId != null && chatId != -1) {
            initializePagedList(chatId!!)
            fetchChatMembers(chatId!!.toString(), 0, 100)
            connectToChatWebSocket(chatId!!)
            fetchMessages(userId!!, 0, PAGE_SIZE)
        } else if (userId != null) {
            getChatsIfExist(userId!!)
            fetchOrCreateChat(userId!!)
        }
    }

    private fun getChatsIfExist(userId: String) {
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
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .build()
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory, config).build()
        subscribeToPaginatedLiveData.call()
    }

    private fun fetchChatMembers(chatId: String, page: Int, size: Int) {
        fetchChatMembersUseCase.compositeDisposable.clear()

        fetchChatMembersResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<User>>() {
            override fun onFail(error: String) {
                hasFetchGroupMembersError = true
                fetchChatMembersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchChatMembersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<User>) {
                hasFetchGroupMembersError = false
                chatMembers = o
                    .filter {
                        it.id != sessionRepository.getUserId()
                    }.map {
                        UserMentionable(
                            it.id,
                            it.name!!,
                            it.avatarMedium!!
                        )
                    }
            }
        }
        fetchChatMembersUseCase.execute(
            callback,
            FetchChatMembersUseCase.Params.forMember(chatId, page, size, null)
        )
    }

    fun filterUserList(query: String, queryToken: QueryToken) {
        if (!chatMembers.isNullOrEmpty()) {
            fetchChatMembersResponse.postValue(
                Resource.success(
                    QueryTokenUserMentionable(
                        queryToken,
                        chatMembers!!.filter {
                            it.fullName.toLowerCase().contains(query.toLowerCase())
                        }
                    )
                )
            )
        }
    }

    fun fetchOrCreateChat(otherUserId: String) {
        handlerReconnect.removeCallbacksAndMessages(null)
        fetchCreateChatResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<CreateChatResponse>() {
            override fun onFail(error: String) {
                hasFetchChatError = true

                if (reconnectingAttempts != RECONNECTING_ATTEMPTS) {
                    Timber.d("fetchOrCreateChat: attemp: ".plus(reconnectingAttempts))
                    reconnectingAttempts++
                    handlerReconnect.postDelayed({
                        fetchOrCreateChat(otherUserId)
                    }, 3000)
                } else {
                    fetchCreateChatResponse.postValue(Resource.error(error, null))
                }
            }

            override fun onHttpException(error: JsonElement) {
                fetchCreateChatResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CreateChatResponse) {
                hasFetchChatError = false
                chatId = o.id.toInt()

                if (pagedListLiveData == null)
                    initializePagedList(o.id.toInt())

                fetchMessages(otherUserId, 0, PAGE_SIZE)
                fetchCreateChatResponse.postValue(Resource.success(o))
                connectToChatWebSocket(o.id.toInt())
            }
        }
        fetchOrCreateChatUseCase.execute(
            callback,
            FetchOrCreateChatUseCase.Params.forUser(otherUserId)
        )
    }

    fun fetchMessages(otherUserId: String, page: Int, size: Int) {
        fetchMessagesResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<DataChatMessageResponse>() {
            override fun onFail(error: String) {
                hasFetchChatError = true

                pageFetchList.remove(page)
                fetchMessagesResponse.postValue(
                    Resource.error
                        (
                        error,
                        DataChatMessageResponse(meta = Meta(page = page))
                    )
                )
            }

            override fun onHttpException(error: JsonElement) {
                hasFetchChatError = true

                fetchMessagesResponse.postValue(
                    Resource.error(
                        error.toString(),
                        DataChatMessageResponse(meta = Meta(page = page))
                    )
                )
            }

            override fun onSuccess(o: DataChatMessageResponse) {
                hasFetchChatError = false

                fetchMessagesResponse.postValue(Resource.success(o))

                uiScope.launch {
                    chatMessageRepository.insertChatMessagesOnDB(o.data!!.chats!!)
                }
            }
        }
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

    fun sendMessage(message: String, image: String?) {
        var parsedMessage = message
        if (mentions.size != 0) {
            for (user in mentions) {
                val mention = "@".plus(user.name)
                val replace = "<@U-".plus(user.id).plus(">")
                parsedMessage = parsedMessage.replace(mention, replace)
            }
        }

        sendMessageResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ChatMessageEntityResponse>() {
            override fun onFail(error: String) {
                sendMessageResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                sendMessageResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ChatMessageEntityResponse) {
                mentions.clear()
                sendMessageResponse.postValue(Resource.success(null))
            }
        }
        sendMessagesUseCase.execute(
            callback, SendMessagesUseCase.Params
                .forMsg(parsedMessage, chatId.toString(), image ?: "")
        )
    }

    fun insertChatDb(chatMessageResponse: ChatMessageEntityResponse) {
        uiScope.launch {
            chatMessageRepository.insertChatMessageOnDB(chatMessageResponse)
        }
    }

    fun checkIfFetchMoreData(
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
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
        fetchChatMembersUseCase.dispose()
        sendMessagesUseCase.dispose()
        fetchOrCreateChatUseCase.dispose()
        fetchMessagesUseCase.dispose()
    }

    private fun connectToChatWebSocket(chatId: Int) {

        chatWebSocket.listener = object : ChatWebSocket.Listener {
            override fun onSocketOpen() {
                fetchMessages(userId!!, 0, PAGE_SIZE)
            }

            override fun onChatMessageReceived(chat: ChatMessageEntityResponse) {
                insertChatDb(chat)
            }
        }

        chatWebSocket.connect(chatId.toString())
    }

    private fun checkIfReconnectionIsNeeded() {
        if (chatId != null && chatId != -1 &&
            (chatWebSocket.getSocketState() == MySocket.State.CONNECT_ERROR ||
                    chatWebSocket.getSocketState() == MySocket.State.RECONNECT_ATTEMPT)
        ) {
            chatWebSocket.stopTryingToReconnect()
            connectToChatWebSocket(chatId!!)
        }
    }

    fun checkErrorsToReconnect() {
        if (hasFetchChatError) {
            fetchOrCreateChat(userId!!)
        } else {
            checkIfReconnectionIsNeeded()
        }

        if (hasFetchGroupMembersError && chatId != null && chatId != -1) {
            fetchChatMembers(chatId!!.toString(), 0, 100)
        }
    }

    fun onDestroy() {
        chatWebSocket.close()
    }

    fun onResume() {
        //Not to show notifications of current chat
        sessionRepository.setCurrentChatId(chatId.toString())
    }

    fun onPause() {
        sessionRepository.setCurrentChatId(null)
    }
}