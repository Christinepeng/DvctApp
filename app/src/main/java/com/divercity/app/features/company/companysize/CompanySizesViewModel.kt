package com.divercity.app.features.company.companysize

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.company.sizes.CompanySizeResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.company.companysize.usecase.FetchCompanySizesUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class CompanySizesViewModel @Inject
constructor(private val fetchCompanySizesUseCase: FetchCompanySizesUseCase) : BaseViewModel() {

    var fetchCompanySizeResponse = MutableLiveData<Resource<List<CompanySizeResponse>>>()

    init {
        fetchJobTypes()
    }

    fun fetchJobTypes() {
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
        compositeDisposable.add(callback)
        fetchCompanySizesUseCase.execute(callback, null)
    }
}
