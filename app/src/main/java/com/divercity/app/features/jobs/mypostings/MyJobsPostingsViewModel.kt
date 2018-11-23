package com.divercity.app.features.jobs.mypostings

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.mypostings.datasource.JobPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class MyJobsPostingsViewModel @Inject
constructor(private val repository: JobPaginatedRepositoryImpl) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobResponse>>()
    lateinit var pagedJobsList: LiveData<PagedList<JobResponse>>
    private lateinit var listingPaginatedJob: Listing<JobResponse>
    private var lastSearch: String? = null

    init {
        fetchJobs(null, "")
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchJobs(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        searchQuery?.let {
            if (it != lastSearch) {
                lastSearch = it
                listingPaginatedJob = repository.fetchData(searchQuery)
                pagedJobsList = listingPaginatedJob.pagedList

                lifecycleOwner?.let { lifecycleOwner ->
                    removeObservers(lifecycleOwner)
                    subscribeToPaginatedLiveData.call()
                }
            }
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedJobsList.removeObservers(lifecycleOwner)
    }
}
