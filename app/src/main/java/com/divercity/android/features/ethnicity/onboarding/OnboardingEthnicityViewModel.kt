package com.divercity.android.features.ethnicity.onboarding

import android.arch.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.profile.profile.User
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class OnboardingEthnicityViewModel @Inject
constructor(private val updateUserProfileUseCase: UpdateUserProfileUseCase,
            private val userRepository: UserRepository,
            private val sessionRepository: SessionRepository) : BaseViewModel() {

    val updateUserProfileResponse = MutableLiveData<Resource<UserResponse>>()

    fun updateUserProfile(ethnicity: String) {
        updateUserProfileResponse.postValue(Resource.loading<UserResponse>(null))

        val callback = object : DisposableObserverWrapper<UserResponse>() {
            override fun onFail(error: String) {
                updateUserProfileResponse.postValue(Resource.error<UserResponse>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                updateUserProfileResponse.postValue(Resource.error<UserResponse>(error.toString(), null))
            }

            override fun onSuccess(o: UserResponse) {
                updateUserProfileResponse.postValue(Resource.success(o))
            }
        }
        val user = User()
        user.ethnicity = ethnicity
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))
    }

    fun getAccountType(): String {
        return sessionRepository.getAccountType()
    }

    override fun onCleared() {
        super.onCleared()
        updateUserProfileUseCase.dispose()
    }
}