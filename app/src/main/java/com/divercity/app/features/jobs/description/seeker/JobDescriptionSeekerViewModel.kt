package com.divercity.app.features.jobs.description.seeker

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.jobs.jobs.usecase.RemoveSavedJobUseCase
import com.divercity.app.features.jobs.jobs.usecase.SaveJobUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDescriptionSeekerViewModel @Inject
constructor(private val removeSavedJobUseCase: RemoveSavedJobUseCase,
            private val saveJobUseCase: SaveJobUseCase) : BaseViewModel() {

    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobResponse>>()

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
}