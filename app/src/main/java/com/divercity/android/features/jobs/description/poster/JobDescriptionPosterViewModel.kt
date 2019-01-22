package com.divercity.android.features.jobs.description.poster

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.jobs.mypostings.usecase.PublishJobUseCase
import com.divercity.android.features.jobs.mypostings.usecase.UnpublishJobUseCase
import com.divercity.android.features.jobs.usecase.DeleteJobUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDescriptionPosterViewModel @Inject
constructor(private val publishJobUseCase: PublishJobUseCase,
            private val unpublishJobUseCase: UnpublishJobUseCase,
            private val deleteJobUseCase : DeleteJobUseCase) : BaseViewModel() {

    var publishUnpublishJobResponse = SingleLiveEvent<Resource<JobResponse>>()
    var deleteJobResponse = SingleLiveEvent<Resource<JobResponse>>()

    fun publishUnpublishJob(jobData: JobResponse) {
        publishUnpublishJobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                publishUnpublishJobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                publishUnpublishJobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                publishUnpublishJobResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)

        jobData.attributes?.publishable?.let {
            if(it)
                unpublishJobUseCase.execute(callback, UnpublishJobUseCase.Params.forPublish(jobData.id!!))
            else
                publishJobUseCase.execute(callback, PublishJobUseCase.Params.forPublish(jobData.id!!))
        }
    }

    fun deleteJob(jobId : String){

        deleteJobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                deleteJobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                deleteJobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                deleteJobResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)

        deleteJobUseCase.execute(callback, DeleteJobUseCase.Params.forDelete(jobId))
    }
}