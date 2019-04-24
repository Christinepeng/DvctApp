package com.divercity.android.features.company.companydetail

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.companydetail.usecase.FetchCompanyUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class CompanyDetailViewModel @Inject
constructor(private val fetchCompanyUseCase: FetchCompanyUseCase) : BaseViewModel() {

    var companyResponse: CompanyResponse? = null
        set(value) {
            companyLiveData.value = value
            field = value
        }

    var companyLiveData = MutableLiveData<CompanyResponse?>()

    var fetchCompanyResponse = SingleLiveEvent<Resource<CompanyResponse>>()

    lateinit var companyId: String

    fun fetchCompany() {
        fetchCompanyResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<CompanyResponse>() {
            override fun onFail(error: String) {
                fetchCompanyResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchCompanyResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CompanyResponse) {
                /*As I am sending the reference we need to update the current reference, so when
                user press back, the reference is updated*/

                companyResponse?.attributes = o.attributes
                companyLiveData.value = companyResponse
                fetchCompanyResponse.postValue(Resource.success(o))
            }
        }
        fetchCompanyUseCase.execute(callback, FetchCompanyUseCase.Params(companyId))
    }

    override fun onCleared() {
        super.onCleared()
        fetchCompanyUseCase.dispose()
    }
}