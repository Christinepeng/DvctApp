package com.divercity.app.features.jobs.description.poster

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.jobs.mypostings.usecase.PublishJobUseCase
import com.divercity.app.features.jobs.mypostings.usecase.UnpublishJobUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDescriptionPosterViewModel @Inject
constructor(private val publishJobUseCase: PublishJobUseCase,
            private val unpublishJobUseCase: UnpublishJobUseCase) : BaseViewModel() {

    var publishUnpublishJobResponse = SingleLiveEvent<Resource<JobResponse>>()

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
}