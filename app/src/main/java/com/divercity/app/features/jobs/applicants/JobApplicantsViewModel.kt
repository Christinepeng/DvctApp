package com.divercity.app.features.jobs.applicants

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import com.divercity.app.features.jobs.applicants.datasource.JobApplicantsPaginatedRepositoryImpl
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