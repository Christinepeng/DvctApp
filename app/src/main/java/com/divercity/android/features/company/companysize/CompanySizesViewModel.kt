package com.divercity.android.features.company.companysize

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.companysize.usecase.FetchCompanySizesUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class CompanySizesViewModel @Inject
constructor(private val fetchCompanySizesUseCase: FetchCompanySizesUseCase) : BaseViewModel() {

    var fetchCompanySizeResponse = MutableLiveData<Resource<List<CompanySizeResponse>>>()

    init {
        fetchCompanySizes()
    }

    fun fetchCompanySizes() {
        fetchCompanySizeResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<CompanySizeResponse>>() {
            override fun onFail(error: String) {
                fetchCompanySizeResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchCompanySizeResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<CompanySizeResponse>) {
                fetchCompanySizeResponse.postValue(Resource.success(o))
            }
        }
        fetchCompanySizesUseCase.execute(callback, null)
    }


    override fun onCleared() {
        super.onCleared()
        fetchCompanySizesUseCase.dispose()
    }
}
