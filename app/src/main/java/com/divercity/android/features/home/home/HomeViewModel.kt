package com.divercity.android.features.home.home

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Event
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.entity.storiesfeatured.StoriesFeaturedResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.divercity.android.features.home.home.datasource.QuestionsPaginatedRepositoryImpl
import com.divercity.android.features.home.home.usecase.FetchFeedRecommendedJobsGroupsUseCase
import com.divercity.android.features.home.home.usecase.FetchUnreadMessagesCountUseCase
import com.divercity.android.features.home.home.usecase.GetStoriesFeatured
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class HomeViewModel @Inject
constructor(
    private val questionsRepository: QuestionsPaginatedRepositoryImpl,
    private val getStoriesFeatured: GetStoriesFeatured,
    private val fetchFeedRecommendedJobsGroupsUseCase: FetchFeedRecommendedJobsGroupsUseCase,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase,
    private val session: SessionRepository,
    private val fetchUnreadMessagesCountUseCase: FetchUnreadMessagesCountUseCase
) : BaseViewModel() {

    var featuredList = MutableLiveData<Resource<List<StoriesFeaturedResponse>>>()
    var questionList: LiveData<PagedList<HomeItem>>
    var fetchRecommendedJobsGroupsResponse = MutableLiveData<Resource<List<RecommendedItem>>>()
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    var fetchUnreadMessagesCountResponse = MutableLiveData<Resource<Int>>()
    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()

    private var questionListing: Listing<HomeItem> = questionsRepository.fetchData()

    val networkState: LiveData<NetworkState> = questionListing.networkState

    val refreshState: LiveData<NetworkState> = questionListing.refreshState

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // To hold tab1 scroll position when fragment dies
    val listState = MutableLiveData<Parcelable>()

    init {
        questionList = questionListing.pagedList
        fetchUnreadMessagesCount()
    }

    fun retry() {
        questionsRepository.retry()
    }

    fun refresh() {
        questionsRepository.refresh()
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
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group.id))
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
        requestToJoinUseCase.execute(
            callback,
            RequestJoinGroupUseCase.Params.toJoin(group.id)
        )
    }

    fun fetchUnreadMessagesCount() {
        fetchUnreadMessagesCountResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Int>() {
            override fun onFail(error: String) {
                fetchUnreadMessagesCountResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchUnreadMessagesCountResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Int) {
                fetchUnreadMessagesCountResponse.postValue(Resource.success(o))
            }
        }
        fetchUnreadMessagesCountUseCase.execute(callback, null)
    }

    override fun onCleared() {
        super.onCleared()
        questionsRepository.clear()
        fetchFeedRecommendedJobsGroupsUseCase.dispose()
        joinGroupUseCase.dispose()
        requestToJoinUseCase.dispose()
    }

    fun onDestroyView() {
        fetchUnreadMessagesCountUseCase.compositeDisposable.clear()
    }
}
