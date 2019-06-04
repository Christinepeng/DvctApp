package com.divercity.android.features.onboarding.selectoccupationofinterests

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.occupationofinterests.OOIResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.selectoccupationofinterests.usecase.FollowOOIUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectOOIViewModel @Inject
constructor(
    repository: OOIPaginatedRepository,
    private val sessionRepository: SessionRepository,
    private val followOOIUseCase: FollowOOIUseCase
) : BaseViewModelPagination<OOIResponse>(repository) {

    val followOOIResponse = MutableLiveData<Resource<User>>()

    init {
        fetchData()
    }

    fun updateUserProfile(ooiIds: List<String>) {
        followOOIResponse.postValue(Resource.loading<User>(null))
        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                followOOIResponse.postValue(Resource.error<User>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                followOOIResponse.postValue(Resource.error<User>(error.toString(), null))
            }

            override fun onSuccess(o: User) {
                followOOIResponse.postValue(Resource.success(o))
            }
        }
        followOOIUseCase.execute(callback, FollowOOIUseCase.Params.forOOI(ooiIds))
    }

    fun getAccountType(): String {
        return sessionRepository.getAccountType()
    }

    override fun onCleared() {
        super.onCleared()
        followOOIUseCase.dispose()
    }
}
