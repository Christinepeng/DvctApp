package com.divercity.android.features.jobs.similarjobs

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.similarjobs.datasource.SimilarJobPaginatedRepositoryImpl
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
