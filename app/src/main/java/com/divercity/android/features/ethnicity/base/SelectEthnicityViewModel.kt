package com.divercity.android.features.ethnicity.base

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.ethnicity.base.usecase.FetchEthniciesUseCase
import com.divercity.android.model.Ethnicity
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class SelectEthnicityViewModel @Inject
constructor(private val fetchEthniciesUseCase: FetchEthniciesUseCase) : BaseViewModel() {

    val fetchEthniciesResponse = MutableLiveData<Resource<List<Ethnicity>>>()

    init {
        fetchEthnicies()
    }

    fun fetchEthnicies() {
        fetchEthniciesResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<Ethnicity>>() {
            override fun onFail(error: String) {
                fetchEthniciesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchEthniciesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<Ethnicity>) {
                fetchEthniciesResponse.postValue(Resource.success(o))
            }
        }
        fetchEthniciesUseCase.execute(callback, null)
    }

    override fun onCleared() {
        super.onCleared()
        fetchEthniciesUseCase.dispose()
    }
}