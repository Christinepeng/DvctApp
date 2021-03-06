package com.divercity.app.features.signup

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.signup.usecase.CheckIsUsernameRegisteredUseCase
import com.divercity.app.features.signup.usecase.SignUpUseCase
import com.divercity.app.features.signup.usecase.UploadProfilePictureUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class SignUpViewModel @Inject
constructor(private val signUpUseCase: SignUpUseCase,
            private val usernameRegisteredUseCase: CheckIsUsernameRegisteredUseCase,
            private val uploadProfilePictureUseCase: UploadProfilePictureUseCase) : BaseViewModel() {

    val signUpResponse = SingleLiveEvent<Resource<Any>>()
    val usernameRegisteredResponse = SingleLiveEvent<Resource<Boolean>>()
    val uploadProfilePictureResponse = SingleLiveEvent<Resource<LoginResponse>>()
    val navigateToSelectUserType = SingleLiveEvent<Boolean>()

    fun signUp(name: String, username: String, email: String, password: String, confirmPassword: String) {

        signUpResponse.value = Resource.loading(null)
        val callback = object : SignUpUseCase.Callback() {
            override fun onFail(msg: String) {
                signUpResponse.value = Resource.error(msg, null)
            }

            override fun onSuccess(response: LoginResponse) {
                signUpResponse.value = Resource.success(response)
            }
        }
        compositeDisposable.add(callback)
        signUpUseCase.execute(callback, SignUpUseCase.Params.forSignUp(
                username,
                name,
                email,
                password,
                confirmPassword))
    }

    fun checkUsernameRegistered(username: String) {

        val callback = object : DisposableObserverWrapper<Boolean>() {

            override fun onFail(error: String) {
            }

            override fun onHttpException(error: JsonElement?) {
            }

            override fun onSuccess(t: Boolean) {
                usernameRegisteredResponse.value = Resource.success(t)
            }
        }
        compositeDisposable.add(callback)
        usernameRegisteredUseCase.execute(callback, CheckIsUsernameRegisteredUseCase.Params.forCheckUsername(username))
    }

    fun uploadPicture(pictureBase64: String) {
        if (pictureBase64 == "")
            navigateToSelectUserType.call()
        else {
            val callback = object : DisposableObserverWrapper<LoginResponse>() {

                override fun onFail(error: String) {
                    uploadProfilePictureResponse.value = Resource.error(error, null)
                }

                override fun onHttpException(error: JsonElement?) {

                }

                override fun onSuccess(t: LoginResponse) {
                    uploadProfilePictureResponse.value = Resource.success(t)
                }
            }
            compositeDisposable.add(callback)
            uploadProfilePictureUseCase.execute(callback, UploadProfilePictureUseCase.Params.forUploadPic(pictureBase64))
        }
    }
}
