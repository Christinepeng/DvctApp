package com.divercity.android.features.jobs.jobposting.sharetogroup

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.jobs.jobposting.sharetogroup.datasource.FollowedGroupsPaginatedRepositoryImpl
import com.divercity.android.features.jobs.jobposting.sharetogroup.usecase.ShareJobGroupsUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class ShareJobGroupViewModel @Inject
constructor(
    private val repository: FollowedGroupsPaginatedRepositoryImpl,
    private val shareJobGroupsUseCase: ShareJobGroupsUseCase
) : BaseViewModel() {

    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    lateinit var listingPaginatedGroups: Listing<GroupResponse>

    val shareJobGroupsResponse = MutableLiveData<Resource<JobResponse>>()
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()

    internal var jobId: String? = null

    private lateinit var lastSearch: String

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedGroups.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedGroups.refreshState

    init {
        fetchFollowedGroups(null, null)
    }

    fun fetchFollowedGroups(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        if (searchQuery == null) {
            lastSearch = ""
            fetchData(lifecycleOwner, lastSearch)
        } else if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner, lastSearch)
        }
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String) {
        repository.clear()

        listingPaginatedGroups = repository.fetchData(searchQuery)
        pagedGroupList = listingPaginatedGroups.pagedList

        lifecycleOwner?.let {
            removeObservers(it)
        }

        subscribeToPaginatedLiveData.call()
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState.removeObservers(lifecycleOwner)
        refreshState.removeObservers(lifecycleOwner)
        pagedGroupList.removeObservers(lifecycleOwner)
    }

    fun shareJobs(groupsIds: List<String>) {
        shareJobGroupsResponse.postValue(Resource.loading<JobResponse>(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                shareJobGroupsResponse.postValue(Resource.error<JobResponse>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                shareJobGroupsResponse.postValue(
                    Resource.error<JobResponse>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: JobResponse) {
                shareJobGroupsResponse.postValue(Resource.success(o))
            }
        }
        shareJobGroupsUseCase.execute(
            callback,
            ShareJobGroupsUseCase.Params.forShare(jobId!!, groupsIds)
        )
    }

    override fun onCleared() {
        super.onCleared()
        shareJobGroupsUseCase.dispose()
        repository.clear()
    }

    fun retry() {
        repository.retry()
    }

    fun refresh() {
        repository.refresh()
    }
}
