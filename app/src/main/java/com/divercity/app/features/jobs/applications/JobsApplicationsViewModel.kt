package com.divercity.app.features.jobs.applications

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.R
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.jobs.applications.datasource.JobApplicationsPaginatedRepositoryImpl
import com.divercity.app.features.jobs.jobs.usecase.RemoveSavedJobUseCase
import com.divercity.app.features.jobs.jobs.usecase.SaveJobUseCase
import com.divercity.app.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobsApplicationsViewModel @Inject
constructor(private val context: Application,
            private val userRepository: UserRepository,
            private val repositoryApplications: JobApplicationsPaginatedRepositoryImpl,
            private val removeSavedJobUseCase: RemoveSavedJobUseCase,
            private val saveJobUseCase: SaveJobUseCase) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobResponse>>()
    var navigateToJobSeekerDescription = SingleLiveEvent<JobResponse>()
    var navigateToJobRecruiterDescription = SingleLiveEvent<JobResponse>()
    lateinit var pagedJobsList: LiveData<PagedList<JobApplicationResponse>>
    private lateinit var listingPaginatedJob: Listing<JobApplicationResponse>
    private var lastSearch: String? = null

    init {
        fetchJobs(null, "")
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repositoryApplications.retry()

    fun refresh() = repositoryApplications.refresh()

    fun fetchJobs(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        searchQuery?.let {
            if (it != lastSearch) {
                lastSearch = it
                listingPaginatedJob = repositoryApplications.fetchData(searchQuery)
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

    fun saveJob(jobData: JobResponse) {
        jobSaveUnsaveResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobSaveUnsaveResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobSaveUnsaveResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobSaveUnsaveResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        saveJobUseCase.execute(callback, SaveJobUseCase.Params.forJobs(jobData.id!!))
    }

    fun removeSavedJob(jobData: JobResponse) {
        jobSaveUnsaveResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobSaveUnsaveResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobSaveUnsaveResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobSaveUnsaveResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        removeSavedJobUseCase.execute(callback, RemoveSavedJobUseCase.Params.forJobs(jobData.id!!))
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
