package com.divercity.android.features.jobs.mypostings

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.jobs.mypostings.datasource.JobPaginatedRepositoryImpl
import com.divercity.android.features.jobs.mypostings.usecase.PublishJobUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class MyJobsPostingsViewModel @Inject
constructor(private val repository: JobPaginatedRepositoryImpl,
            private val publishJobUseCase: PublishJobUseCase) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var publishJobResponse = SingleLiveEvent<Resource<JobResponse>>()
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

    fun forceFetchMyPosting(){
        listingPaginatedJob = repository.fetchData("")
        pagedJobsList = listingPaginatedJob.pagedList
    }

    fun publishJob(jobData: JobResponse) {
        publishJobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                publishJobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                publishJobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                publishJobResponse.postValue(Resource.success(o))
            }
        }
        publishJobUseCase.execute(callback, PublishJobUseCase.Params.forPublish(jobData.id!!))
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedJobsList.removeObservers(lifecycleOwner)
    }

    override fun onCleared() {
        super.onCleared()
        publishJobUseCase.dispose()
    }
}
