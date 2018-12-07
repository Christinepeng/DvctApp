package com.divercity.app.features.jobs.similarjobs

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.similarjobs.datasource.SimilarJobPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SimilarJobListViewModel @Inject
constructor(
        private val repositorySimilar: SimilarJobPaginatedRepositoryImpl) : BaseViewModel() {

    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobResponse>>()
    lateinit var pagedJobsList: LiveData<PagedList<JobResponse>>
    private lateinit var listingPaginatedJob: Listing<JobResponse>

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repositorySimilar.retry()

    fun refresh() = repositorySimilar.refresh()

    fun fetchSimilarJobs(jobId: String) {
        listingPaginatedJob = repositorySimilar.fetchData(jobId)
        pagedJobsList = listingPaginatedJob.pagedList
    }
}
