package com.divercity.app.features.jobs.description.detail

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.jobs.jobs.usecase.RemoveSavedJobUseCase
import com.divercity.app.features.jobs.jobs.usecase.SaveJobUseCase
import com.divercity.app.features.jobs.usecase.FetchJobByIdUseCase
import com.divercity.app.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDetailViewModel @Inject
constructor(private val removeSavedJobUseCase: RemoveSavedJobUseCase,
            private val saveJobUseCase: SaveJobUseCase,
            private val fetchJobByIdUseCase: FetchJobByIdUseCase,
            val userRepository: UserRepository) : BaseViewModel() {

    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobResponse>>()
    var fetchJobByIdResponse = MutableLiveData<Resource<JobResponse>>()
    var showJobData = MutableLiveData<Resource<JobResponse>>()

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
                showJobData.postValue(Resource.success(o))
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
                showJobData.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        removeSavedJobUseCase.execute(callback, RemoveSavedJobUseCase.Params.forJobs(jobData.id!!))
    }

    fun fetchJobById(jobId: String) {
        fetchJobByIdResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                fetchJobByIdResponse.postValue(Resource.error(error, JobResponse(id = jobId)))
            }

            override fun onHttpException(error: JsonElement) {
                fetchJobByIdResponse.postValue(Resource.error(error.toString(), JobResponse(id = jobId)))
            }

            override fun onSuccess(o: JobResponse) {
                showJobData.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchJobByIdUseCase.execute(callback, FetchJobByIdUseCase.Params.forJob(jobId))
    }

    fun isLoggedUserJobSeeker() : Boolean{
        return userRepository.isLoggedUserJobSeeker()
    }
}