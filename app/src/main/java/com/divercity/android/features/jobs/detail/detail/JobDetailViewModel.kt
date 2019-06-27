package com.divercity.android.features.jobs.detail.detail

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.jobs.jobs.usecase.RemoveSavedJobUseCase
import com.divercity.android.features.jobs.jobs.usecase.SaveJobUseCase
import com.divercity.android.features.jobs.usecase.FetchJobByIdUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDetailViewModel @Inject
constructor(
    private val removeSavedJobUseCase: RemoveSavedJobUseCase,
    private val saveJobUseCase: SaveJobUseCase,
    private val fetchJobByIdUseCase: FetchJobByIdUseCase,
    val sessionRepository: SessionRepository
) : BaseViewModel() {

    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<Unit>>()
    var fetchJobResponse = MutableLiveData<Resource<JobResponse>>()

    lateinit var jobId: String
    var jobLiveData = MutableLiveData<JobResponse>()

    fun setJob(job: JobResponse) {
        jobId = job.id!!
        jobLiveData.postValue(job)
    }

    fun getJob(): JobResponse {
        return jobLiveData.value!!
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
                onJobSave(true)
                jobSaveUnsaveResponse.postValue(Resource.success(null))
            }
        }
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
                onJobSave(false)
                jobSaveUnsaveResponse.postValue(Resource.success(null))
            }
        }
        removeSavedJobUseCase.execute(callback, RemoveSavedJobUseCase.Params.forJobs(jobData.id!!))
    }

    fun fetchJob() {
        fetchJobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                fetchJobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchJobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                setJob(o)
                fetchJobResponse.postValue(Resource.success(o))
            }
        }
        fetchJobByIdUseCase.execute(callback, FetchJobByIdUseCase.Params.forJob(jobId))
    }

    fun onJobSave(state: Boolean) {
        val job = jobLiveData.value
        job?.attributes?.isBookmarkedByCurrent = state
        jobLiveData.postValue(job)
    }

    fun onCancelJobApplication() {
        val job = jobLiveData.value
        job?.attributes?.isAppliedByCurrent = false
        jobLiveData.postValue(job)
    }

    fun isLoggedUserJobSeeker(): Boolean {
        return sessionRepository.isLoggedUserJobSeeker()
    }

    fun onJobApplied(){
        val job = jobLiveData.value
        job?.attributes?.isAppliedByCurrent = true
        jobLiveData.postValue(job)
    }

    override fun onCleared() {
        super.onCleared()
        removeSavedJobUseCase.dispose()
        fetchJobByIdUseCase.dispose()
        saveJobUseCase.dispose()
    }
}