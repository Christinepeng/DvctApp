package com.divercity.android.features.education.degree

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.education.degree.usecase.FetchDegreesUseCase
import com.divercity.android.model.Degree
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class SelectDegreeViewModel @Inject
constructor(private val fetchDegreesUseCase: FetchDegreesUseCase) : BaseViewModel() {

    val fetchDegreesResponse = MutableLiveData<Resource<List<Degree>>>()

    init {
        fetchDegrees()
    }

    fun fetchDegrees() {
        fetchDegreesResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<Degree>>() {
            override fun onFail(error: String) {
                fetchDegreesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchDegreesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<Degree>) {
                fetchDegreesResponse.postValue(Resource.success(o))
            }
        }
        fetchDegreesUseCase.execute(callback, null)
    }

    override fun onCleared() {
        super.onCleared()
        fetchDegreesUseCase.dispose()
    }
}