package com.divercity.android.features.signup

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.signup.usecase.CheckIsUsernameRegisteredUseCase
import com.divercity.android.features.signup.usecase.SignUpUseCase
import com.divercity.android.features.signup.usecase.UploadProfilePictureUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class SignUpViewModel @Inject
constructor(private val signUpUseCase: SignUpUseCase,
            private val usernameRegisteredUseCase: CheckIsUsernameRegisteredUseCase,
            private val uploadProfilePictureUseCase: UploadProfilePictureUseCase)
    : BaseViewModel() {

    val signUpResponse = SingleLiveEvent<Resource<Any>>()
    val usernameRegisteredResponse = SingleLiveEvent<Resource<Boolean>>()
    val uploadProfilePictureResponse = SingleLiveEvent<Resource<User>>()
    val navigateToSelectUserType = SingleLiveEvent<Boolean>()

    fun signUp(name: String, email: String, password: String, confirmPassword: String) {

        signUpResponse.value = Resource.loading(null)
        val callback = object : SignUpUseCase.Callback() {
            override fun onFail(msg: String) {
                signUpResponse.value = Resource.error(msg, null)
            }

            override fun onSuccess(response: User) {
                signUpResponse.value = Resource.success(response)
            }
        }
        signUpUseCase.execute(callback, SignUpUseCase.Params.forSignUp(
                name,
                email,
                password,
                confirmPassword))
    }

//    fun checkUsernameRegistered(username: String) {
//
//        val callback = object : DisposableObserverWrapper<Boolean>() {
//
//            override fun onFail(error: String) {
//            }
//
//            override fun onHttpException(code: Int, error: JsonElement?) {
//            }
//
//            override fun onSuccess(t: Boolean) {
//                usernameRegisteredResponse.value = Resource.success(t)
//            }
//        }
//        usernameRegisteredUseCase.execute(callback, CheckIsUsernameRegisteredUseCase.Params.forCheckUsername(username))
//    }

    fun uploadPicture(pictureBase64: String) {
        if (pictureBase64 == "")
            navigateToSelectUserType.call()
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
        usernameRegisteredUseCase.dispose()
        uploadProfilePictureUseCase.dispose()
        signUpUseCase.dispose()
    }
}
