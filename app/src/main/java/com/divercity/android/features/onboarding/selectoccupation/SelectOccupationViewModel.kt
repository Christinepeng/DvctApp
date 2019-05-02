package com.divercity.android.features.onboarding.selectoccupation

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectOccupationViewModel @Inject
constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {
    val updateUserProfileResponse = MutableLiveData<Resource<User>>()

    val accountType: String
        get() = sessionRepository.getAccountType()

    fun updateUserProfile(occupation: String) {
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
        val user = UserProfileEntity()
        user.occupation = occupation
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params(user))
    }

    override fun onCleared() {
        super.onCleared()
        updateUserProfileUseCase.dispose()
    }
}
