package com.divercity.android.features.company.diversityrating

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewResponse
import com.divercity.android.features.company.companydetail.usecase.FetchCompanyUseCase
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class DiversityRatingViewModel @Inject
constructor(
    private val fetchCompanyUseCase: FetchCompanyUseCase,
    repository: DiversityReviewsPaginatedRepository
) : BaseViewModelPagination<CompanyDiversityReviewResponse>(repository) {

    private var companyId: String? = null

    fun fetchCompanyDiversityReviews(companyId: String) {
        if (this.companyId == null) {
            this.companyId = companyId
            (repository as DiversityReviewsPaginatedRepository).setCompanyId(companyId)
            fetchData()
        }
    }


//    var companyLiveData = MutableLiveData<CompanyResponse?>()
//
//    var fetchCompanyResponse = SingleLiveEvent<Resource<CompanyResponse>>()
//
//    fun fetchCompany(companyId: String) {
//        fetchCompanyResponse.postValue(Resource.loading(null))
//        val callback = object : DisposableObserverWrapper<CompanyResponse>() {
//            override fun onFail(error: String) {
//                fetchCompanyResponse.postValue(Resource.error(error, null))
//            }
//
//            override fun onHttpException(code: Int, error: JsonElement) {
//                fetchCompanyResponse.postValue(Resource.error(error.toString(), null))
//            }
//
//            override fun onSuccess(o: CompanyResponse) {
//                companyLiveData.postValue(o)
//                fetchCompanyResponse.postValue(Resource.success(o))
//            }
//        }
//        fetchCompanyUseCase.execute(callback, FetchCompanyUseCase.Params(companyId))
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        fetchCompanyUseCase.dispose()
//    }
}