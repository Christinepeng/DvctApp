package com.divercity.android.features.onboarding.personalinfo

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.Resource.Companion.error
import com.divercity.android.data.Resource.Companion.loading
import com.divercity.android.data.Resource.Companion.success
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.features.signup.usecase.CheckIsUsernameRegisteredUseCase
import com.divercity.android.features.signup.usecase.UploadProfilePictureUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */
class PersonalInfoViewModel @Inject
constructor(
    var updateUserProfileUseCase: UpdateUserProfileUseCase,
    var sessionRepository: SessionRepository,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase
) : BaseViewModel() {

    var dataUpdateUser = MutableLiveData<Resource<User>>()
    val uploadProfilePictureResponse = SingleLiveEvent<Resource<User>>()


    fun updateUserProfile(typeId: String?) {
        dataUpdateUser.postValue(
            loading<User>(
                null
            )
        )
        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                dataUpdateUser.postValue(
                    error<User>(
                        error,
                        null
                    )
                )
            }

            override fun onHttpException(error: JsonElement) {
                dataUpdateUser.postValue(
                    error<User>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: User) {
                dataUpdateUser.postValue(
                    success(
                        o
                    )
                )
            }
        }
        val user = UserProfileEntity()
        user.accountType = typeId
        updateUserProfileUseCase.execute(
            callback,
            UpdateUserProfileUseCase.Params(user)
        )
    }

    fun uploadPicture(pictureBase64: String) {
        if (pictureBase64 == "")
//            navigateToSelectUserType.call()
        else {
            val callback = object : DisposableObserverWrapper<User>() {

                override fun onFail(error: String) {
                    uploadProfilePictureResponse.value = Resource.error(error, null)
                }

                override fun onHttpException(error: JsonElement) {

                }

                override fun onSuccess(t: User) {
                    uploadProfilePictureResponse.value = Resource.success(t)
                }
            }
            uploadProfilePictureUseCase.execute(callback, UploadProfilePictureUseCase.Params.forUploadPic(pictureBase64))
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateUserProfileUseCase.dispose()
    }

}