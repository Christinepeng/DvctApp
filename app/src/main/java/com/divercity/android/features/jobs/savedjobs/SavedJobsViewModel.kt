package com.divercity.android.features.jobs.savedjobs

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.savedjobs.datasource.SavedJobPaginatedRepositoryImpl
import com.divercity.android.repository.user.UserRepository
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
    var lastSearch: String? = null

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

    fun fetchJobsForced(lifecycleOwner: LifecycleOwner?){
        listingPaginatedJob = repository.fetchData("")
        pagedJobsList = listingPaginatedJob.pagedList

        lifecycleOwner?.let { lifecycleOwner ->
            removeObservers(lifecycleOwner)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedJobsList.removeObservers(lifecycleOwner)
    }
}
