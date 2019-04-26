package com.divercity.android.features.dialogs.ratecompany

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.rating.Rating
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.ratecompany.usecase.RateCompanyUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class RateCompanyDiversityDialogViewModel @Inject
constructor(
    private val rateCompanyUseCase: RateCompanyUseCase
) : BaseViewModel() {

    var rateCompanyResponse = SingleLiveEvent<Resource<CompanyDiversityReviewResponse>>()

    fun rateCompany(companyId: String, rate: Int, review: String?) {
        rateCompanyResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<CompanyDiversityReviewResponse>() {
            override fun onFail(error: String) {
                rateCompanyResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                rateCompanyResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CompanyDiversityReviewResponse) {
                rateCompanyResponse.postValue(Resource.success(o))
            }
        }
        rateCompanyUseCase.execute(
            callback,
            RateCompanyUseCase.Params(companyId, Rating(rate, review))
        )
    }

    override fun onCleared() {
        super.onCleared()
        rateCompanyUseCase.dispose()
    }
}