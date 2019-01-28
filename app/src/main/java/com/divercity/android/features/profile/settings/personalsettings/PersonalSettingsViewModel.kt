package com.divercity.android.features.profile.settings.personalsettings

import android.content.Context
import com.divercity.android.R
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.usecase.UpdateFCMTokenUseCase
import com.divercity.android.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class PersonalSettingsViewModel @Inject
constructor(
    private val context : Context,
    private val userRepository: UserRepository,
    private val updateFCMTokenUseCase: UpdateFCMTokenUseCase
) : BaseViewModel() {

    val updateFCMTokenResponse = SingleLiveEvent<Resource<Boolean>>()
//    val usernameRegisteredResponse = SingleLiveEvent<Resource<Boolean>>()
//    val uploadProfilePictureResponse = SingleLiveEvent<Resource<UserResponse>>()
//    val navigateToSelectUserType = SingleLiveEvent<Boolean>()

    fun getProfilePicture(): String? {
        return userRepository.getAvatarUrl()
    }

    fun enableNotifications(enabled: Boolean) {
        updateFCMTokenResponse.postValue(Resource.loading(null))
        if (!userRepository.getDeviceId().isNullOrEmpty() && !userRepository.getFCMToken().isNullOrEmpty()) {
            val callback = object : DisposableObserverWrapper<Void>() {
                override fun onFail(error: String) {
                    updateFCMTokenResponse.postValue(Resource.error(error, enabled))
                }

                override fun onHttpException(error: JsonElement) {
                    updateFCMTokenResponse.postValue(Resource.error(error.toString(), enabled))

                }

                override fun onSuccess(o: Void) {
                    updateFCMTokenResponse.postValue(Resource.success(enabled))
                }
            }
            updateFCMTokenUseCase.execute(
                callback, UpdateFCMTokenUseCase.Params.forDevice(
                    userRepository.getDeviceId()!!,
                    userRepository.getFCMToken()!!,
                    enabled
                )
            )
        } else {
            updateFCMTokenResponse.postValue(Resource.error(context.resources.getString(R.string.error_notifications), null))
        }
    }
//
//    fun checkUsernameRegistered(username: String) {
//
//        val callback = object : DisposableObserverWrapper<Boolean>() {
//
//            override fun onFail(error: String) {
//            }
//
//            override fun onHttpException(error: JsonElement?) {
//            }
//
//            override fun onSuccess(t: Boolean) {
//                usernameRegisteredResponse.value = Resource.success(t)
//            }
//        }
//        compositeDisposable.add(callback)
//        usernameRegisteredUseCase.execute(callback, CheckIsUsernameRegisteredUseCase.Params.forCheckUsername(username))
//    }
//
//    fun uploadPicture(pictureBase64: String) {
//        if (pictureBase64 == "")
//            navigateToSelectUserType.call()
//        else {
//            val callback = object : DisposableObserverWrapper<UserResponse>() {
//
//                override fun onFail(error: String) {
//                    uploadProfilePictureResponse.value = Resource.error(error, null)
//                }
//
//                override fun onHttpException(error: JsonElement?) {
//
//                }
//
//                override fun onSuccess(t: UserResponse) {
//                    uploadProfilePictureResponse.value = Resource.success(t)
//                }
//            }
//            compositeDisposable.add(callback)
//            uploadProfilePictureUseCase.execute(callback, UploadProfilePictureUseCase.Params.forUploadPic(pictureBase64))
//        }
//    }
}
