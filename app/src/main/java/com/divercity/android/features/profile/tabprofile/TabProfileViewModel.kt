package com.divercity.android.features.profile.tabprofile

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.profile.profile.User
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.selectinterests.usecase.FetchInterestsUseCase
import com.divercity.android.features.onboarding.selectinterests.usecase.FollowInterestsUseCase
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabProfileViewModel @Inject
constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val sessionRepository: SessionRepository,
    private val fetchInterestsUseCase: FetchInterestsUseCase,
    private val followInterestsUseCase: FollowInterestsUseCase
) : BaseViewModel() {

    var fetchInterestsResponse = MutableLiveData<Resource<List<InterestsResponse>>>()
    val updateUserProfileResponse = SingleLiveEvent<Resource<UserResponse>>()

    fun getEthnicity(): String? {
        return sessionRepository.getEthnicity()
    }

    fun getGender(): String? {
        return sessionRepository.getGender()
    }

    fun getIndustries(): String? {
        return sessionRepository.getIndustries()
    }

    fun getAgeRange(): String? {
        return sessionRepository.getAgeRange()
    }

    fun getLocation(): String? {
        return sessionRepository.getLocation()
    }

    fun getUserType() : String?{
        return sessionRepository.getUserType()
    }

    fun updateEthnicity(ethnicity: String?) {
        val user = User()
        user.ethnicity = ethnicity
        updateUserProfile(user)
    }

    fun updateGender(gender: String?) {
        val user = User()
        user.gender = gender
        updateUserProfile(user)
    }

    fun updateAgeRange(ageRange: String?) {
        val user = User()
        user.ageRange = ageRange
        updateUserProfile(user)
    }

    fun updateLocation(location: LocationResponse) {
        val user = User()
        user.city = location.attributes?.name
        user.country = location.attributes?.countryName
        updateUserProfile(user)
    }

    fun fetchInterests() {
        fetchInterestsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<InterestsResponse>>() {
            override fun onFail(error: String) {
                fetchInterestsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchInterestsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<InterestsResponse>) {
                val interests = sessionRepository.getInterests()
                interests?.let {

                    for(i in o){
                        if(interests.contains(i.id?.toInt())){
                            i.isSelected = true
                        }
                    }
                }
                fetchInterestsResponse.postValue(Resource.success(o))
            }
        }
        fetchInterestsUseCase.execute(callback, null)
    }

    fun updateUserProfile(user: User) {
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
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))
    }

    override fun onCleared() {
        super.onCleared()
        updateUserProfileUseCase.dispose()
        fetchInterestsUseCase.dispose()
        followInterestsUseCase.dispose()
    }
}
