package com.divercity.android.features.jobs.jobposting.jobtype

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.job.jobtype.JobTypeResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.jobs.jobposting.jobtype.usecase.FetchJobTypesUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobTypeViewModel @Inject
constructor(private val fetchJobTypesUseCase: FetchJobTypesUseCase) : BaseViewModel() {

    var fetchJobTypesResponse = MutableLiveData<Resource<List<JobTypeResponse>>>()

    init {
        fetchJobTypes()
    }

    fun fetchJobTypes() {
        fetchJobTypesResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<JobTypeResponse>>() {
            override fun onFail(error: String) {
                fetchJobTypesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchJobTypesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<JobTypeResponse>) {
                fetchJobTypesResponse.postValue(Resource.success(o))
            }
        }
        fetchJobTypesUseCase.execute(callback, Any())
    }

    override fun onCleared() {
        super.onCleared()
        fetchJobTypesUseCase.dispose()
    }
}
