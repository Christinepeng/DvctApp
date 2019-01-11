package com.divercity.app.features.home.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Event
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.message.MessageResponse
import com.divercity.app.data.entity.questions.QuestionResponse
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.usecase.JoinGroupUseCase
import com.divercity.app.features.groups.usecase.RequestJoinGroupUseCase
import com.divercity.app.features.home.home.feed.datasource.QuestionsPaginatedRepositoryImpl
import com.divercity.app.features.home.home.usecase.FetchRecommendedJobsGroupsUseCase
import com.divercity.app.features.home.home.usecase.GetStoriesFeatured
import com.divercity.app.repository.chat.ChatRepositoryImpl
import com.divercity.app.repository.user.LoggedUserRepositoryImpl
import com.google.gson.JsonElement
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject
constructor(
        private val questionsRepository: QuestionsPaginatedRepositoryImpl,
        private val getStoriesFeatured: GetStoriesFeatured,
        private val loggedUserRepository: LoggedUserRepositoryImpl,
        private val chatRepositoryImpl: ChatRepositoryImpl,
        private val fetchRecommendedJobsGroupsUseCase: FetchRecommendedJobsGroupsUseCase,
        private val joinGroupUseCase: JoinGroupUseCase,
        private val requestToJoinUseCase : RequestJoinGroupUseCase
) : BaseViewModel() {

    var featuredList = MutableLiveData<Resource<List<StoriesFeaturedResponse>>>()
    var questionList: LiveData<PagedList<QuestionResponse>>
    var fetchRecommendedJobsGroupsResponse = MutableLiveData<Resource<List<RecommendedItem>>>()
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()

    private var questionListing: Listing<QuestionResponse> = questionsRepository.fetchData()

    val networkState: LiveData<NetworkState> = questionListing.networkState

    val refreshState: LiveData<NetworkState> = questionListing.refreshState

    init {
        questionList = questionListing.pagedList
        fetchRecommendedGroupsJobs()
    }

    fun retry() {
        questionsRepository.retry()
    }

    fun refresh() {
        questionsRepository.refresh()
    }

    fun fetchRecommendedGroupsJobs() {
        fetchRecommendedJobsGroupsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<RecommendedItem>>() {
            override fun onFail(error: String) {
                fetchRecommendedJobsGroupsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchRecommendedJobsGroupsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<RecommendedItem>) {
                fetchRecommendedJobsGroupsResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchRecommendedJobsGroupsUseCase.execute(
                callback,
                FetchRecommendedJobsGroupsUseCase.Params.forJobs(0,5,null))
    }

    fun getFeatured() {
        val disposable = object : DisposableObserver<List<StoriesFeaturedResponse>>() {
            override fun onNext(storiesFeaturedResponses: List<StoriesFeaturedResponse>) {
                featuredList.postValue(Resource.success(storiesFeaturedResponses))
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
        compositeDisposable.add(disposable)
        getStoriesFeatured.execute(disposable, null)
    }

    fun joinGroup(group: GroupResponse) {
        joinGroupResponse.postValue(Event(Resource.loading(null)))

        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onSuccess(t: Boolean) {
                joinGroupResponse.postValue(Event(Resource.success(t)))
            }

            override fun onFail(error: String) {
                joinGroupResponse.postValue(Event(Resource.error(error, null)))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Event(Resource.error(error.toString(), null)))
            }
        }

        compositeDisposable.add(callback)
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group))
    }

    fun requestToJoinGroup(group: GroupResponse) {
        requestToJoinResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<MessageResponse>() {
            override fun onFail(error: String) {
                requestToJoinResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                requestToJoinResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: MessageResponse) {
                requestToJoinResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        requestToJoinUseCase.execute(callback,
                RequestJoinGroupUseCase.Params.toJoin(group.id))
    }

    override fun onCleared() {
        super.onCleared()
        questionsRepository.clear()
    }

    fun clearUserData() {
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            chatRepositoryImpl.deleteChatDB()
            chatRepositoryImpl.deleteChatMessagesDB()
        }
        loggedUserRepository.clearUserData()
    }
}
