package com.divercity.android.features.location.onboarding

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 09/11/2018.
 */

class OnboardingLocationViewModel @Inject
constructor(private val updateUserProfileUseCase: UpdateUserProfileUseCase,
            private val sessionRepository: SessionRepository) : BaseViewModel() {

    val updateUserProfileResponse = MutableLiveData<Resource<User>>()

    fun updateUserProfile(location: LocationResponse) {
        updateUserProfileResponse.postValue(Resource.loading<User>(null))

        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                updateUserProfileResponse.postValue(Resource.error<User>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                updateUserProfileResponse.postValue(Resource.error<User>(error.toString(), null))
            }

            override fun onSuccess(o: User) {
                updateUserProfileResponse.postValue(Resource.success(o))
            }
        }
        val user = UserProfileEntity()
        user.city = location.attributes?.name
        user.country = location.attributes?.countryName
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params(user))
    }

    fun getAccountType(): String {
        return sessionRepository.getAccountType()
    }

    override fun onCleared() {
        super.onCleared()
        updateUserProfileUseCase.dispose()
    }
}