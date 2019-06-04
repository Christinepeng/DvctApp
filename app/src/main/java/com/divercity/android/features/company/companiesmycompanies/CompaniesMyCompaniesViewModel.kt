package com.divercity.android.features.company.companiesmycompanies

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.mycompanies.usecase.FetchMyCompaniesUseCase
import com.divercity.android.features.company.selectcompany.base.CompanyPaginatedRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class CompaniesMyCompaniesViewModel @Inject
constructor(
    repository: CompanyPaginatedRepository,
    private val fetchMyCompaniesUseCase: FetchMyCompaniesUseCase
) :
    BaseViewModelPagination<CompanyResponse>(repository) {

    val fetchMyCompaniesResponse = MutableLiveData<Resource<List<CompanyResponse>>>()

    val showMyCompaniesSection = MutableLiveData<Boolean>()

    init {
        showMyCompaniesSection.value = false
        fetchData(null, "")
        fetchMyCompanies()
    }

    fun fetchMyCompanies() {
        fetchMyCompaniesResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<CompanyResponse>>() {
            override fun onFail(error: String) {
                fetchMyCompaniesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchMyCompaniesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<CompanyResponse>) {
                fetchMyCompaniesResponse.postValue(Resource.success(o))
            }
        }
        fetchMyCompaniesUseCase.execute(callback, FetchMyCompaniesUseCase.Params.forJobs(0, 3))
    }
}
