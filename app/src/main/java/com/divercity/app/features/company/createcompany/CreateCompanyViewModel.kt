package com.divercity.app.features.company.createcompany

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.company.sizes.CompanySizeResponse
import com.divercity.app.data.entity.industry.IndustryResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.company.createcompany.usecase.CreateCompanyUseCase
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
        compositeDisposable.add(callback)
        createCompanyUseCase.execute(callback, CreateCompanyUseCase.Params.forCompany(name, sizeId, desc, headquarters, industryId, logo))
    }
}