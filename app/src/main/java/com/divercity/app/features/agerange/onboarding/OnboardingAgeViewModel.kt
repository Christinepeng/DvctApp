package com.divercity.app.features.agerange.onboarding

import android.arch.lifecycle.MutableLiveData

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.user.response.UserResponse
import com.divercity.app.data.entity.profile.profile.User
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.app.repository.user.UserRepository
import com.google.gson.JsonElement

import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class OnboardingAgeViewModel @Inject
constructor(private val updateUserProfileUseCase: UpdateUserProfileUseCase,
            private val userRepository: UserRepository) : BaseViewModel() {

    val updateUserProfileResponse = MutableLiveData<Resource<UserResponse>>()

    var ageRangeSelected: String? = null

    fun updateUserProfileWithSelectedAgeRange() {
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
        compositeDisposable.add(callback)
        val user = User()
        user.ageRange = ageRangeSelected
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))
    }

    fun getAccountType(): String {
        return userRepository.getAccountType()!!
    }
}
