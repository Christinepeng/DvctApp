package com.divercity.app.features.location.onboarding

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.data.entity.profile.profile.User
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.app.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 09/11/2018.
 */

class OnboardingLocationViewModel @Inject
constructor(private val updateUserProfileUseCase: UpdateUserProfileUseCase,
            private val userRepository: UserRepository) : BaseViewModel() {

    val updateUserProfileResponse = MutableLiveData<Resource<LoginResponse>>()

    fun updateUserProfile(location: LocationResponse) {
        updateUserProfileResponse.postValue(Resource.loading<LoginResponse>(null))

        val callback = object : DisposableObserverWrapper<LoginResponse>() {
            override fun onFail(error: String) {
                updateUserProfileResponse.postValue(Resource.error<LoginResponse>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                updateUserProfileResponse.postValue(Resource.error<LoginResponse>(error.toString(), null))
            }

            override fun onSuccess(o: LoginResponse) {
                updateUserProfileResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        val user = User()
        user.city = location.attributes?.name
        user.country = location.attributes?.countryName
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))
    }

    fun getAccountType(): String {
        return userRepository.getAccountType()!!
    }
}