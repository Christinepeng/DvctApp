package com.divercity.android.features.groups.groupanswers

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.MySocket
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.answer.response.AnswerEntityResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.groupanswers.model.AnswersPageModel
import com.divercity.android.features.groups.groupanswers.usecase.FetchAnswersUseCase
import com.divercity.android.features.groups.groupanswers.usecase.FetchQuestionByIdUseCase
import com.divercity.android.features.groups.groupanswers.usecase.SendNewAnswerUseCase
import com.divercity.android.features.groups.groupdetail.usecase.FetchGroupMembersUseCase
import com.divercity.android.model.Question
import com.divercity.android.model.user.User
import com.divercity.android.model.usermentionable.QueryTokenUserMentionable
import com.divercity.android.model.usermentionable.UserMentionable
import com.divercity.android.repository.group.GroupRepository
import com.divercity.android.socket.AnswersWebSocket
import com.google.gson.JsonElement
import com.linkedin.android.spyglass.tokenization.QueryToken
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
    private val fetchGroupMembersUseCase: FetchGroupMembersUseCase,
    private val fetchAnswersUseCase: FetchAnswersUseCase,
    private val sendNewAnswerUseCase: SendNewAnswerUseCase,
    private val groupRepository: GroupRepository,
    private val fetchQuestionByIdUseCase: FetchQuestionByIdUseCase
) : BaseViewModel() {

    var pageFetchList = ArrayList<Int>()

    var fetchAnswersResponse = SingleLiveEvent<Resource<AnswersPageModel>>()
    var sendNewAnswerResponse = SingleLiveEvent<Resource<AnswerEntityResponse>>()

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var pagedListLiveData: LiveData<PagedList<AnswerEntityResponse>>? = null
    var fetchGroupMembersResponse = SingleLiveEvent<Resource<QueryTokenUserMentionable>>()
    var fetchQuestionByIdResponse = SingleLiveEvent<Resource<Any?>>()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var handlerReconnect = Handler()
    var reconnectingAttempts = 0

    lateinit var questionId: String
    var questionLiveData = MutableLiveData<Question?>()

    var hasFetchQuestionsError = false

    var mentions = HashSet<User>()

    companion object {
        const val RECONNECTING_ATTEMPTS = 4
        private const val PAGE_SIZE = 30
        private const val THRESHOLD = 10
    }

    fun start(questionId: String) {
        this.questionId = questionId
        initializePagedList()
        connectToAnswersWebSocket()
        fetchAnswers(0, PAGE_SIZE, "")
    }

    fun initializePagedList() {
        val dataSourceFactory = groupRepository.getPagedAnswersByQuestionId(questionId.toInt())
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .build()
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory, config).build()
        subscribeToPaginatedLiveData.call()
    }

    fun fetchAnswers(page: Int, size: Int, searchQuery: String) {
        fetchAnswersResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<AnswerEntityResponse>>() {
            override fun onFail(error: String) {
                hasFetchQuestionsError = true

                pageFetchList.remove(page)
                fetchAnswersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                hasFetchQuestionsError = true

                fetchAnswersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<AnswerEntityResponse>) {
                hasFetchQuestionsError = false

                fetchAnswersResponse.postValue(Resource.success(AnswersPageModel(o, page)))

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

    fun fetchQuestionById() {
        fetchQuestionByIdResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Question>() {
            override fun onFail(error: String) {
                fetchQuestionByIdResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchQuestionByIdResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Question) {
                fetchQuestionByIdResponse.postValue(Resource.success(null))
                questionLiveData.value = o
            }
        }

        fetchQuestionByIdUseCase.execute(
            callback, FetchQuestionByIdUseCase.Params(questionId)
        )
    }

    fun sendNewAnswer(answer: String, image: String?) {

        var images: List<String>? = null
        image?.let {
            images = listOf(it)
        }

        sendNewAnswerResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<AnswerEntityResponse>() {
            override fun onFail(error: String) {
                sendNewAnswerResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                sendNewAnswerResponse.postValue(Resource.error(error.toString(), null))
            }


            override fun onSuccess(o: AnswerEntityResponse) {
                mentions.clear()
                sendNewAnswerResponse.postValue(Resource.success(null))
            }
        }
        sendNewAnswerUseCase.execute(
            callback, SendNewAnswerUseCase.Params
                .forAnswer(images, answer, questionId)
        )
    }

    fun insertAnswerDb(answer: AnswerEntityResponse) {
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
                fetchAnswers(page, PAGE_SIZE, "")
            }
        } else if (visibleItemCount + firstVisibleItemPosition == totalItemCount) {
//          We add one to check if the next page has been fetched
            val page = totalItemCount / PAGE_SIZE + 1
            if (!pageFetchList.contains(page)) {
                pageFetchList.add(page)
                fetchAnswers(page, PAGE_SIZE, "")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        fetchGroupMembersUseCase.dispose()
        fetchAnswersUseCase.dispose()
    }

    private fun connectToAnswersWebSocket() {

        answersWebSocket.listener = object : AnswersWebSocket.Listener {

            override fun onAnswerReceived(answer: AnswerEntityResponse) {
                insertAnswerDb(answer)
            }
        }

        answersWebSocket.connect(questionId)
    }

    private fun checkIfReconnectionIsNeeded() {
        if (answersWebSocket.getSocketState() == MySocket.State.CONNECT_ERROR ||
            answersWebSocket.getSocketState() == MySocket.State.RECONNECT_ATTEMPT
        ) {
            answersWebSocket.stopTryingToReconnect()
            connectToAnswersWebSocket()
        }
    }

    fun checkErrorsToReconnect() {
        if (hasFetchQuestionsError) {
            fetchAnswers(0, PAGE_SIZE, "")
        } else {
            checkIfReconnectionIsNeeded()
        }
    }

    fun fetchGroupMembers(
        groupId: String,
        page: Int,
        size: Int,
        query: String?,
        queryToken: QueryToken
    ) {
        fetchGroupMembersUseCase.compositeDisposable.clear()

        fetchGroupMembersResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<User>>() {
            override fun onFail(error: String) {
                fetchGroupMembersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupMembersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<User>) {
                val listMentionable =
                    o.map {
                        UserMentionable(
                            it.id,
                            it.name!!,
                            it.avatarMedium!!
                        )
                    }
                fetchGroupMembersResponse.postValue(
                    Resource.success(
                        QueryTokenUserMentionable(
                            queryToken,
                            listMentionable
                        )
                    )
                )
            }
        }
        fetchGroupMembersUseCase.execute(
            callback,
            FetchGroupMembersUseCase.Params.forGroups(groupId, page, size, query)
        )
    }

    fun onDestroy() {
        answersWebSocket.close()
    }

    fun onResume() {

    }

    fun onPause() {

    }
}