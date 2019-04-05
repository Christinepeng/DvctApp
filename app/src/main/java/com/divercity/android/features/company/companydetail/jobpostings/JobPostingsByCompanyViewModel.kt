package com.divercity.android.features.company.companydetail.jobpostings

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.company.companydetail.jobpostings.datasource.JobPostingsByCompanyRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobPostingsByCompanyViewModel @Inject
constructor(
    private val repository: JobPostingsByCompanyRepositoryImpl
) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobResponse>>()
    lateinit var pagedJobsList: LiveData<PagedList<JobResponse>>
    private var listingPaginatedJob: Listing<JobResponse>? = null

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob!!.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob!!.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchData(companyId: String) {
        if (listingPaginatedJob == null) {
            listingPaginatedJob = repository.fetchData(companyId)
            pagedJobsList = listingPaginatedJob!!.pagedList
        }
    }
}
