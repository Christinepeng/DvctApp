package com.divercity.android.features.home.home

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import com.divercity.android.core.utils.Event
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.divercity.android.features.home.home.usecase.DiscardRecommendedGroupsUseCase
import com.divercity.android.features.home.home.usecase.DiscardRecommendedJobsUseCase
import com.divercity.android.features.home.home.usecase.FetchUnreadMessagesCountUseCase
import com.divercity.android.features.jobs.jobs.usecase.RemoveSavedJobUseCase
import com.divercity.android.features.jobs.jobs.usecase.SaveJobUseCase
import com.divercity.android.model.position.GroupPosition
import com.divercity.android.model.position.JobPosition
import com.google.gson.JsonElement
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class HomeViewModel @Inject
constructor(
    repository: QuestionsJobPaginatedRepository,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase,
    private val fetchUnreadMessagesCountUseCase: FetchUnreadMessagesCountUseCase,
    private val discardRecommendedGroupsUseCase: DiscardRecommendedGroupsUseCase,
    private val discardRecommendedJobsUseCase: DiscardRecommendedJobsUseCase,
    private val removeSavedJobUseCase: RemoveSavedJobUseCase,
    private val saveJobUseCase: SaveJobUseCase
) : BaseViewModelPagination<HomeItem>(repository) {

    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    var fetchUnreadMessagesCountResponse = MutableLiveData<Resource<Int>>()
    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()
    var discardRecommendedJobsResponse = SingleLiveEvent<Resource<JobPosition>>()
    var discardRecommendedGroupsResponse = SingleLiveEvent<Resource<GroupPosition>>()
    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobPosition>>()

    // To hold tab1 scroll position when fragment dies
    val listState = MutableLiveData<Parcelable>()

    val showRecommendedSection = MutableLiveData<Boolean>()
    private var newMessageDisposable: Disposable

    init {
        showRecommendedSection.value = false
        fetchData()
        fetchUnreadMessagesCount()

        newMessageDisposable = RxBus.listen(RxEvent.OnNewMessageReceived::class.java).subscribe {
            fetchUnreadMessagesCount()
        }
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

    fun discardRecommendedGroup(groupPos: GroupPosition) {
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

    fun discardRecommendedJobs(jobPos: JobPosition) {
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

    fun saveUnsaveJob(save: Boolean, jobPos: JobPosition) {
        if (save)
            saveJob(jobPos)
        else
            removeSavedJob(jobPos)
    }

    fun saveJob(jobPos: JobPosition) {
        jobSaveUnsaveResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobSaveUnsaveResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobSaveUnsaveResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobPos.job.attributes?.isBookmarkedByCurrent = o.attributes?.isBookmarkedByCurrent
                jobSaveUnsaveResponse.postValue(Resource.success(jobPos))
            }
        }
        saveJobUseCase.execute(callback, SaveJobUseCase.Params.forJobs(jobPos.job.id!!))
    }

    fun removeSavedJob(jobPos: JobPosition) {
        jobSaveUnsaveResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobSaveUnsaveResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobSaveUnsaveResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobPos.job.attributes?.isBookmarkedByCurrent = o.attributes?.isBookmarkedByCurrent
                jobSaveUnsaveResponse.postValue(Resource.success(jobPos))
            }
        }
        removeSavedJobUseCase.execute(
            callback,
            RemoveSavedJobUseCase.Params.forJobs(jobPos.job.id!!)
        )
    }

    override fun onCleared() {
        super.onCleared()
        joinGroupUseCase.dispose()
        requestToJoinUseCase.dispose()
        if (!newMessageDisposable.isDisposed) newMessageDisposable.dispose()
    }

    fun onDestroyView() {
        fetchUnreadMessagesCountUseCase.compositeDisposable.clear()
    }
}
