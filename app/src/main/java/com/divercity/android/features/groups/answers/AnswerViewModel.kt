package com.divercity.android.features.groups.answers

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Handler
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.MySocket
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.chat.chat.usecase.FetchChatMembersUseCase
import com.divercity.android.features.groups.answers.model.Question
import com.divercity.android.features.groups.answers.usecase.FetchAnswersUseCase
import com.divercity.android.features.groups.answers.usecase.SendNewAnswerUseCase
import com.divercity.android.repository.group.GroupRepository
import com.divercity.android.socket.AnswersWebSocket
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by lucas on 24/12/2018.
 */

class AnswerViewModel @Inject
constructor(
    private val answersWebSocket: AnswersWebSocket,
    private val fetchChatMembersUseCase: FetchChatMembersUseCase,
    private val fetchAnswersUseCase: FetchAnswersUseCase,
    private val sendNewAnswerUseCase: SendNewAnswerUseCase,
    private val groupRepository: GroupRepository
) : BaseViewModel() {

    var pageFetchList = ArrayList<Int>()
    var chatMembers: List<UserResponse>? = null

    var fetchAnswersResponse = SingleLiveEvent<Resource<List<AnswerResponse>>>()
    var sendNewAnswerResponse = SingleLiveEvent<Resource<AnswerResponse>>()

    var fetchCreateChatResponse = SingleLiveEvent<Resource<CreateChatResponse>>()
    var fetchMessagesResponse = SingleLiveEvent<Resource<List<ChatMessageResponse>>>()
    var fetchChatMembersResponse = SingleLiveEvent<Resource<List<UserResponse>>>()
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var pagedListLiveData: LiveData<PagedList<AnswerResponse>>? = null

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var handlerReconnect = Handler()
    var reconnectingAttempts = 0

    var question: Question? = null
    var questionId: Int = -1

    var hasFetchChatError = false
    var hasFetchGroupMembersError = false

    var mentions = HashSet<UserResponse>()

    companion object {
        const val RECONNECTING_ATTEMPTS = 4
        private const val PAGE_SIZE = 30
        private const val THRESHOLD = 10
    }

    fun start() {
        questionId = question!!.id!!.toInt()
        initializePagedList(questionId)
        connectToAnswersWebSocket(questionId)
        fetchAnswers(question!!.id!!, 0, PAGE_SIZE, "")
    }

    fun initializePagedList(questionId: Int) {
        val dataSourceFactory = groupRepository.getPagedAnswersByQuestionId(questionId)
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .build()
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory, config).build()
        subscribeToPaginatedLiveData.call()
    }

    fun filterUserList(filter: String) {
        if (!chatMembers.isNullOrEmpty())
            fetchChatMembersResponse.postValue(
                Resource.success(
                    chatMembers?.filter {
                        it.userAttributes?.name!!.toLowerCase().contains(filter.toLowerCase())
                    })
            )
    }

    fun fetchAnswers(questionId: String, page: Int, size: Int, searchQuery: String) {
        fetchAnswersResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<AnswerResponse>>() {
            override fun onFail(error: String) {
                pageFetchList.remove(page)
                fetchAnswersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchAnswersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<AnswerResponse>) {
                fetchAnswersResponse.postValue(Resource.success(o))

                uiScope.launch {
                    groupRepository.insertAnswers(o)
                }
            }
        }
        fetchAnswersUseCase.execute(
            callback, FetchAnswersUseCase.Params
                .forAnswers(
                    questionId,
                    page,
                    size,
                    searchQuery
                )
        )
    }

    fun sendNewAnswer(answer: String, image: String?) {
//        var parsedMessage = message
//        if (mentions.size != 0) {
//            for (user in mentions) {
//                val mention = "@".plus(user.userAttributes?.name)
//                val replace = "<@U-".plus(user.id).plus(">")
//                parsedMessage = parsedMessage.replace(mention, replace)
//            }
//        }

        var images: List<String>? = null
        image?.let {
            images = listOf(it)
        }

        sendNewAnswerResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<AnswerResponse>() {
            override fun onFail(error: String) {
                sendNewAnswerResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                sendNewAnswerResponse.postValue(Resource.error(error.toString(), null))
            }


            override fun onSuccess(o: AnswerResponse) {
                mentions.clear()
                sendNewAnswerResponse.postValue(Resource.success(null))
            }
        }
        sendNewAnswerUseCase.execute(
            callback, SendNewAnswerUseCase.Params
                .forAnswer(images, answer, question!!.id!!)
        )
    }

    fun insertAnswerDb(answer: AnswerResponse) {
        uiScope.launch {
            groupRepository.insertAnswer(answer)
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
                fetchAnswers(question!!.id!!, page, PAGE_SIZE, "")
            }
        } else if (visibleItemCount + firstVisibleItemPosition == totalItemCount) {
//          We add one to check if the next page has been fetched
            val page = totalItemCount / PAGE_SIZE + 1
            if (!pageFetchList.contains(page)) {
                pageFetchList.add(page)
                fetchAnswers(question!!.id!!, page, PAGE_SIZE, "")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        fetchChatMembersUseCase.dispose()
        fetchAnswersUseCase.dispose()
    }

    private fun connectToAnswersWebSocket(chatId: Int) {

        answersWebSocket.listener = object : AnswersWebSocket.Listener {

            override fun onAnswerReceived(answer: AnswerResponse) {
                insertAnswerDb(answer)
            }
        }

        answersWebSocket.connect(chatId.toString())
    }

    fun checkIfReconnectionIsNeeded() {
        if (questionId != -1 && answersWebSocket.getSocketState() == MySocket.State.CONNECT_ERROR) {
            answersWebSocket.stopTryingToReconnect()
            connectToAnswersWebSocket(questionId)
        }
    }

    fun checkErrorsToReconnect() {
//        if (hasFetchChatError) {
//            fetchOrCreateChat(userId!!)
//        } else {
//            checkIfReconnectionIsNeeded()
//        }
//
//        if (hasFetchGroupMembersError && chatId != null && chatId != -1) {
//            fetchChatMembers(chatId!!.toString(), 0, 100)
//        }
    }

    fun onDestroy() {
        answersWebSocket.close()
    }

    fun onResume() {

    }

    fun onPause() {

    }
}