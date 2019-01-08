package com.divercity.app.features.profile.tabprofile

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.data.entity.profile.profile.User
import com.divercity.app.data.entity.user.response.UserResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.app.features.profile.usecase.FetchUserDataUseCase
import com.divercity.app.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabProfileViewModel @Inject
constructor(private val fetchUserDataUseCase: FetchUserDataUseCase,
            private val updateUserProfileUseCase: UpdateUserProfileUseCase,
            private val userRepository: UserRepository) : BaseViewModel() {

    val updateUserProfileResponse = SingleLiveEvent<Resource<UserResponse>>()

    fun getEthnicity() : String? {
        return userRepository.getEthnicity()
    }

    fun getGender() : String?{
        return userRepository.getGender()
    }

    fun getIndustries() : String? {
        return userRepository.getIndustry()
    }

    fun getAgeRange() : String? {
        return userRepository.getAgeRange()
    }

    fun getLocation() : String? {
        return userRepository.getLocation()
    }

    fun updateEthnicity(ethnicity: String?) {
        val user = User()
        user.ethnicity = ethnicity
        updateUserProfile(user)
    }

    fun updateGender(gender : String?){
        val user = User()
        user.gender = gender
        updateUserProfile(user)
    }

    fun updateAgeRange(ageRange : String?){
        val user = User()
        user.ageRange = ageRange
        updateUserProfile(user)
    }

    fun updateLocation(location : LocationResponse){
        val user = User()
        user.city = location.attributes?.name
        user.country = location.attributes?.countryName
        updateUserProfile(user)
    }

    fun updateUserProfile(user : User){
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
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))

    }
}
