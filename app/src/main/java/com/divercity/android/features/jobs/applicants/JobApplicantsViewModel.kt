package com.divercity.android.features.jobs.applicants

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import com.divercity.android.features.jobs.applicants.datasource.JobApplicantsPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobApplicantsViewModel @Inject
constructor(private val repository: JobApplicantsPaginatedRepositoryImpl) : BaseViewModel() {

    lateinit var pagedApplicantsList: LiveData<PagedList<JobApplicationResponse>>
    private lateinit var listingPaginatedJob: Listing<JobApplicationResponse>

    fun fetchApplicants(jobId: String) {
        listingPaginatedJob = repository.fetchData(jobId)
        pagedApplicantsList = listingPaginatedJob.pagedList
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}