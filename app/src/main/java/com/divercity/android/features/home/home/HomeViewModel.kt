package com.divercity.android.features.home.home

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.Event
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.divercity.android.features.home.home.usecase.DiscardRecommendedGroupsUseCase
import com.divercity.android.features.home.home.usecase.DiscardRecommendedJobsUseCase
import com.divercity.android.features.home.home.usecase.FetchUnreadMessagesCountUseCase
import com.divercity.android.model.position.GroupPositionModel
import com.divercity.android.model.position.JobPositionModel
import com.google.gson.JsonElement
import javax.inject.Inject

class HomeViewModel @Inject
constructor(
    repository: QuestionsJobPaginatedRepository,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase,
    private val fetchUnreadMessagesCountUseCase: FetchUnreadMessagesCountUseCase,
    private val discardRecommendedGroupsUseCase: DiscardRecommendedGroupsUseCase,
    private val discardRecommendedJobsUseCase: DiscardRecommendedJobsUseCase
) : BaseViewModelPagination<HomeItem>(repository) {

    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    var fetchUnreadMessagesCountResponse = MutableLiveData<Resource<Int>>()
    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()
    var discardRecommendedJobsResponse = SingleLiveEvent<Resource<JobPositionModel>>()
    var discardRecommendedGroupsResponse = SingleLiveEvent<Resource<GroupPositionModel>>()

    // To hold tab1 scroll position when fragment dies
    val listState = MutableLiveData<Parcelable>()

    init {
        fetchData()
        fetchUnreadMessagesCount()
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

    fun discardRecommendedGroup(groupPos: GroupPositionModel) {
        discardRecommendedGroupsResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onSuccess(t: Unit) {
                discardRecommendedGroupsResponse.postValue(Resource.success(groupPos))
            }

            override fun onFail(error: String) {
                discardRecommendedGroupsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                discardRecommendedGroupsResponse.postValue(
                    Resource.error(
                        error.toString(),
                        null
                    )
                )
            }
        }
        discardRecommendedGroupsUseCase.execute(
            callback,
            DiscardRecommendedGroupsUseCase.Params.toDiscard(listOf(groupPos.group.id))
        )
    }

    fun discardRecommendedJobs(jobPos: JobPositionModel) {
        discardRecommendedJobsResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onSuccess(t: Unit) {
                discardRecommendedJobsResponse.postValue(Resource.success(jobPos))
            }

            override fun onFail(error: String) {
                discardRecommendedJobsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                discardRecommendedJobsResponse.postValue(Resource.error(error.toString(), null))
            }
        }
        discardRecommendedJobsUseCase.execute(
            callback,
            DiscardRecommendedJobsUseCase.Params.toDiscard(listOf(jobPos.job.id!!))
        )
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
        joinGroupUseCase.dispose()
        requestToJoinUseCase.dispose()
    }

    fun onDestroyView() {
        fetchUnreadMessagesCountUseCase.compositeDisposable.clear()
    }
}
