package com.divercity.app.features.jobs.saved

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.R
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.saved.job.SavedJobPaginatedRepositoryImpl
import com.divercity.app.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SavedJobsViewModel @Inject
constructor(private val context: Application,
            private val repository: SavedJobPaginatedRepositoryImpl,
            private val userRepository: UserRepository) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var navigateToJobSeekerDescription = SingleLiveEvent<JobResponse>()
    var navigateToJobRecruiterDescription = SingleLiveEvent<JobResponse>()
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

    fun onJobClickNavigateToNext(job : JobResponse) {
        if (userRepository.getAccountType() != null &&
                (userRepository.getAccountType().equals(context.getString(R.string.hiring_manager_id)) ||
                        userRepository.getAccountType().equals(context.getString(R.string.recruiter_id)))
        )
            navigateToJobRecruiterDescription.value = job
        else
            navigateToJobSeekerDescription.value = job
    }
}
