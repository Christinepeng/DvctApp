package com.divercity.app.features.home.jobs.saved

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.JobResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.home.jobs.jobs.usecase.RemoveSavedJobUseCase
import com.divercity.app.features.home.jobs.jobs.usecase.SaveJobUseCase
import com.divercity.app.features.home.jobs.saved.job.SavedJobPaginatedRepositoryImpl
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SavedJobsViewModel @Inject
constructor(private val repository: SavedJobPaginatedRepositoryImpl,
            private val removeSavedJobUseCase: RemoveSavedJobUseCase,
            private val saveJobUseCase: SaveJobUseCase) : BaseViewModel() {

    var jobResponse = MutableLiveData<Resource<JobResponse>>()
    var pagedJobsList: LiveData<PagedList<JobResponse>>? = null
    var listingPaginatedJob: Listing<JobResponse>? = null
    var strSearchQuery : String? = null

    init {
        fetchJobs(null)
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob!!.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob!!.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchJobs(searchQuery : String?) {
        listingPaginatedJob = repository.fetchData(searchQuery)
        pagedJobsList = listingPaginatedJob?.pagedList
    }

    fun saveJob(jobData: JobResponse) {
        jobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        saveJobUseCase.execute(callback, SaveJobUseCase.Params.forJobs(jobData.id!!))
    }

    fun removeSavedJob(jobData: JobResponse) {
        jobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        removeSavedJobUseCase.execute(callback, RemoveSavedJobUseCase.Params.forJobs(jobData.id!!))
    }
}
