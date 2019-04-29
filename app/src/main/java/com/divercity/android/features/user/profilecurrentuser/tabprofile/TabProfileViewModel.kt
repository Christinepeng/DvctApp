package com.divercity.android.features.user.profilecurrentuser.tabprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.data.entity.workexperience.response.WorkExperienceResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.selectinterests.usecase.FetchInterestsUseCase
import com.divercity.android.features.onboarding.selectinterests.usecase.FollowInterestsUseCase
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.features.user.usecase.FetchWorkExperiencesUseCase
import com.divercity.android.model.user.User
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
    private val followInterestsUseCase: FollowInterestsUseCase,
    private val fetchWorkExperiencesUseCase: FetchWorkExperiencesUseCase
) : BaseViewModel() {

    var fetchInterestsResponse = MutableLiveData<Resource<List<InterestsResponse>>>()
    val updateUserProfileResponse = SingleLiveEvent<Resource<User>>()
    var fetchWorkExperiencesResponse = MutableLiveData<Resource<List<WorkExperienceResponse>>>()

    fun getUserType(): String? {
        return sessionRepository.getUserType()
    }

    fun getCurrentUser(): LiveData<User> {
        return sessionRepository.getUserDB()
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

                    for (i in o) {
                        if (interests.contains(i.id?.toInt())) {
                            i.isSelected = true
                        }
                    }
                }
                fetchInterestsResponse.postValue(Resource.success(o))
            }
        }
        fetchInterestsUseCase.execute(callback, null)
    }

    fun fetchWorkExperiences() {
        fetchWorkExperiencesResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<WorkExperienceResponse>>() {
            override fun onFail(error: String) {
                fetchWorkExperiencesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchWorkExperiencesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<WorkExperienceResponse>) {
                fetchWorkExperiencesResponse.postValue(Resource.success(o))
            }
        }
        fetchWorkExperiencesUseCase.execute(
            callback,
            FetchWorkExperiencesUseCase.Params.to(sessionRepository.getUserId())
        )
    }

    fun updateUserProfile(user: UserProfileEntity) {
        updateUserProfileResponse.postValue(Resource.loading<User>(null))

        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                updateUserProfileResponse.postValue(Resource.error<User>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                updateUserProfileResponse.postValue(
                    Resource.error<User>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: User) {
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
