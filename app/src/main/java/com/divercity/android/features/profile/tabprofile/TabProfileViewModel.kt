package com.divercity.android.features.profile.tabprofile

import android.arch.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.profile.profile.User
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.selectinterests.usecase.FetchInterestsUseCase
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.features.profile.usecase.FetchUserDataUseCase
import com.divercity.android.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabProfileViewModel @Inject
constructor(private val fetchUserDataUseCase: FetchUserDataUseCase,
            private val updateUserProfileUseCase: UpdateUserProfileUseCase,
            private val userRepository: UserRepository,
            private val fetchInterestsUseCase: FetchInterestsUseCase) : BaseViewModel() {

    var fetchInterestsResponse = MutableLiveData<Resource<List<InterestsResponse>>>()
    val updateUserProfileResponse = SingleLiveEvent<Resource<UserResponse>>()

    init {
        fetchInterests()
    }

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

    private fun fetchInterests() {
        fetchInterestsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<InterestsResponse>>() {
            override fun onFail(error: String) {
                fetchInterestsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchInterestsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<InterestsResponse>) {
                fetchInterestsResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchInterestsUseCase.execute(callback, null)
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
