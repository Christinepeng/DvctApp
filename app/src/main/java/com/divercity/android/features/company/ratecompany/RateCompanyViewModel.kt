package com.divercity.android.features.company.ratecompany

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.rating.Rating
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewEntityResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.ratecompany.usecase.FetchReviewUseCase
import com.divercity.android.features.company.ratecompany.usecase.RateCompanyUseCase
import com.divercity.android.features.company.ratecompany.usecase.UpdateCompanyReviewUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class RateCompanyViewModel @Inject
constructor(
    private val rateCompanyUseCase: RateCompanyUseCase,
    private val fetchReviewUseCase: FetchReviewUseCase,
    private val updateCompanyReviewUseCase: UpdateCompanyReviewUseCase
) : BaseViewModel() {

    var companyLiveData = MutableLiveData<CompanyResponse>()
    var fetchReviewResponse = MutableLiveData<Resource<CompanyDiversityReviewEntityResponse>>()

    var rateCompanyResponse = SingleLiveEvent<Resource<CompanyDiversityReviewEntityResponse>>()

    fun rateCompany(
        genderRate: Int,
        raceRate: Int,
        ageRate: Int,
        sexOrRate: Int,
        bodRate: Int,
        review: String
    ) {
        rateCompanyResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<CompanyDiversityReviewEntityResponse>() {
            override fun onFail(error: String) {
                rateCompanyResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                rateCompanyResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CompanyDiversityReviewEntityResponse) {
                rateCompanyResponse.postValue(Resource.success(o))
            }
        }
        rateCompanyUseCase.execute(
            callback,
            RateCompanyUseCase.Params(
                companyLiveData.value?.id!!,
                Rating(raceRate, sexOrRate, genderRate, bodRate, ageRate, review)
            )
        )
    }

    fun updateReview(
        genderRate: Int,
        raceRate: Int,
        ageRate: Int,
        sexOrRate: Int,
        bodRate: Int,
        review: String
    ) {
        rateCompanyResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<CompanyDiversityReviewEntityResponse>() {
            override fun onFail(error: String) {
                rateCompanyResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                rateCompanyResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: CompanyDiversityReviewEntityResponse) {
                rateCompanyResponse.postValue(Resource.success(o))
            }
        }
        updateCompanyReviewUseCase.execute(
            callback,
            UpdateCompanyReviewUseCase.Params(
                companyLiveData.value?.id!!,
                Rating(raceRate, sexOrRate, genderRate, bodRate, ageRate, review)
            )
        )
    }

    fun fetchReview() {
        companyLiveData.value?.let {
            fetchReviewResponse.postValue(Resource.loading(null))
            val callback =
                object : DisposableObserverWrapper<CompanyDiversityReviewEntityResponse>() {
                    override fun onFail(error: String) {
                        fetchReviewResponse.postValue(Resource.error(error, null))
                    }

                    override fun onHttpException(error: JsonElement) {
                        fetchReviewResponse.postValue(Resource.error(error.toString(), null))
                    }

                    override fun onSuccess(o: CompanyDiversityReviewEntityResponse) {
                        fetchReviewResponse.postValue(Resource.success(o))
                    }
                }
            fetchReviewUseCase.execute(
                callback,
                FetchReviewUseCase.Params(it.id)
            )
        } ?: run { fetchReviewResponse.postValue(Resource.error("Error", null)) }

    }

    fun setCompany(c: CompanyResponse) {
        companyLiveData.value = c
    }

    override fun onCleared() {
        super.onCleared()
        rateCompanyUseCase.dispose()
    }
}