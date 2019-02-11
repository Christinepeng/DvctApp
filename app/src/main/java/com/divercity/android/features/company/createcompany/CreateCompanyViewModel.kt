package com.divercity.android.features.company.createcompany

import android.arch.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.createcompany.usecase.CreateCompanyUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 05/11/2018.
 */

class CreateCompanyViewModel @Inject
constructor(private val createCompanyUseCase: CreateCompanyUseCase) : BaseViewModel() {

    var createCompanyResponse = MutableLiveData<Resource<Void>>()

    var industry : IndustryResponse?= null
    var companySize : CompanySizeResponse? = null

    fun createCompany(name: String,
                      sizeId: String,
                      desc: String,
                      headquarters: String,
                      industryId: String,
                      logo: String) {
        createCompanyResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onFail(error: String) {
                createCompanyResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                createCompanyResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Boolean) {
                createCompanyResponse.postValue(Resource.success(null))
            }
        }
        createCompanyUseCase.execute(callback, CreateCompanyUseCase.Params.forCompany(name, sizeId, desc, headquarters, industryId, logo))
    }

    override fun onCleared() {
        super.onCleared()
        createCompanyUseCase.dispose()
    }
}