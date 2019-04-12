package com.divercity.android.features.gender.onboarding

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
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
 * Created by lucas on 17/10/2018.
 */

class OnboardingGenderViewModel @Inject
constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {
    val updateUserProfileResponse = MutableLiveData<Resource<UserResponse>>()

    val accountType: String
        get() = sessionRepository.getAccountType()

    fun updateUserProfile(gender: String) {
        updateUserProfileResponse.postValue(Resource.loading<UserResponse>(null))
        val callback = object : DisposableObserverWrapper<UserResponse>() {
            override fun onFail(error: String) {
                updateUserProfileResponse.postValue(Resource.error<UserResponse>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                updateUserProfileResponse.postValue(
                    Resource.error<UserResponse>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: UserResponse) {
                updateUserProfileResponse.postValue(Resource.success(o))
            }
        }
        val user = User()
        user.gender = gender
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))
    }

    override fun onCleared() {
        super.onCleared()
        updateUserProfileUseCase.dispose()
    }
}
