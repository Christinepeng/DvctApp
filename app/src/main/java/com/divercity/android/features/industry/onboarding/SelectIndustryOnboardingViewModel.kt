package com.divercity.android.features.industry.onboarding

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.industry.base.IndustryPaginatedRepository
import com.divercity.android.features.industry.onboarding.usecase.FollowIndustriesUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectIndustryOnboardingViewModel @Inject
constructor(repository: IndustryPaginatedRepository,
            private val followIndustriesUseCase: FollowIndustriesUseCase,
            private val sessionRepository: SessionRepository) :
    BaseViewModelPagination<IndustryResponse>(repository) {

    val followIndustriesResponse = MutableLiveData<Resource<User>>()

    init {
        fetchData()
    }

    fun getAccountType() = sessionRepository.getAccountType()

    fun followIndustries(industriesSelected: List<String>) {
        followIndustriesResponse.postValue(Resource.loading<User>(null))
        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                followIndustriesResponse.postValue(Resource.error<User>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                followIndustriesResponse.postValue(Resource.error<User>(error.toString(), null))
            }

            override fun onSuccess(o: User) {
                followIndustriesResponse.postValue(Resource.success(o))
            }
        }
        followIndustriesUseCase.execute(
            callback,
            FollowIndustriesUseCase.Params.forIndustry(industriesSelected)
        )
    }

    override fun onCleared() {
        super.onCleared()
        followIndustriesUseCase.dispose()
    }
}
