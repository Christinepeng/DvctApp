package com.divercity.android.features.signup

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.divercity.android.R
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Resource
import com.divercity.android.data.Resource.Companion.error
import com.divercity.android.data.Resource.Companion.loading
import com.divercity.android.data.Resource.Companion.success
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.login.step1.usecase.*
import com.divercity.android.features.signup.usecase.SignUpPageUseCase
//import com.divercity.android.features.signup.usecase.SignUpUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

/**
 * Created by lucas on 26/09/2018.
 */
class SignUpPageViewModel @Inject constructor(
    private val signUpUseCase: SignUpPageUseCase,
    private val application: Application,
    private val loginEmailUseCase: LoginEmailUseCase,
    private val requestResetPasswordUseCase: RequestResetPasswordUseCase,
    private val checkIskEmailRegisteredUseCase: CheckIskEmailRegisteredUseCase,
    private val loginFacebookUseCase: LoginFacebookUseCase,
    private val loginGoogleUseCase: LoginGoogleUseCase
) :
    BaseViewModel() {
    val signUpResponse = SingleLiveEvent<Resource<User>>()
    val checkEmailRegisteredResponse = SingleLiveEvent<Resource<Boolean>>()
    val loginEmailResponse = SingleLiveEvent<Resource<User>>()
    val requestResetPasswordResponse = SingleLiveEvent<Resource<Unit>>()
    val loginFacebookResponse = SingleLiveEvent<Resource<User>>()
    val loginGoogleResponse = SingleLiveEvent<Resource<User>>()
//    val navigateToSignUp = SingleLiveEvent<Any>()
//    val navigateToLogin = SingleLiveEvent<Any>()
    val navigateToSelectUserType = SingleLiveEvent<Boolean>()

    fun signUp(name: String, email: String, password: String) {

        signUpResponse.value = Resource.loading(null)
        val callback = object : SignUpPageUseCase.Callback() {
            override fun onFail(msg: String) {
                signUpResponse.value = Resource.error(msg, null)
            }

            override fun onSuccess(response: User) {
                signUpResponse.value = Resource.success(response)
            }
        }
        signUpUseCase.execute(callback, SignUpPageUseCase.Params.forSignUp(
            name,
            email,
            password))
    }

    fun checkIfEmailRegistered(email: String) {
        if (Util.isValidEmail(email.trim { it <= ' ' })) {
            checkEmailRegisteredResponse.setValue(
                loading<Boolean>(
                    null
                )
            )
            val disposableObserver: DisposableObserver<Boolean> = object : DisposableObserver<Boolean>() {
                override fun onNext(r: Boolean) {
                    checkEmailRegisteredResponse.setValue(
                        success(
                            r
                        )
                    )
//                    if (r) navigateToLogin.call() else navigateToSignUp.call()
                }

                override fun onError(e: Throwable) {
                    checkEmailRegisteredResponse.setValue(
                        error<Boolean>(
                            e.message!!,
                            null
                        )
                    )
                }

                override fun onComplete() {}
            }
            checkIskEmailRegisteredUseCase.execute(
                disposableObserver,
                CheckIskEmailRegisteredUseCase.Params.forCheckEmail(email)
            )
        } else checkEmailRegisteredResponse.setValue(
            error<Boolean>(
                application.resources.getString(R.string.insert_valid_email),
                null
            )
        )
    }

//    fun loginEmail(email: String?, password: String?) {
//        if (password != null && password != "") {
//            loginEmailResponse.value = loading<User>(null)
//            val callback = object : LoginEmailUseCase.Callback() {
//                override fun onFail(error: String) {
//                    loginEmailResponse.value = error<User>(error, null)
//                }
//
//                override fun onSuccess(response: User) {
//                    loginEmailResponse.value = success(response)
//                }
//            }
//            loginEmailUseCase.execute(callback, LoginEmailUseCase.Params.forLogin(email, password))
//        } else {
//            loginEmailResponse.setValue(
//                error<User>(
//                    application.resources.getString(R.string.insert_valid_password),
//                    null
//                )
//            )
//        }
//    }

    fun requestResetPassword(email: String) {
        requestResetPasswordResponse.value = loading(null)
        val callback = object : DisposableObserverWrapper<Unit>() {

            override fun onFail(error: String) {
                requestResetPasswordResponse.value = error(error, null)
            }

            override fun onHttpException(error: JsonElement) {
                requestResetPasswordResponse.value = error(error.toString(), null)
            }

            override fun onSuccess(t: Unit) {
                requestResetPasswordResponse.value = success(t)
            }
        }
        requestResetPasswordUseCase.execute(callback, RequestResetPasswordUseCase.Params.to(email))
    }

    fun loginFacebook(token: String?) {
        loginFacebookResponse.setValue(
            loading<User>(
                null
            )
        )
        val disposable: DisposableObserverWrapper<User> = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                loginFacebookResponse.setValue(
                    error<User>(
                        error,
                        null
                    )
                )
            }

            override fun onHttpException(error: JsonElement) {
                loginFacebookResponse.setValue(
                    error<User>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(loginResponse: User) {
                loginFacebookResponse.setValue(
                    success(
                        loginResponse
                    )
                )
            }
        }
        loginFacebookUseCase.execute(disposable, LoginFacebookUseCase.Params.forFacebook(token))
    }

    fun loginGoogle(token: String?) {
        loginGoogleResponse.setValue(
            loading<User>(
                null
            )
        )
        val disposable: DisposableObserverWrapper<User> = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                loginGoogleResponse.setValue(
                    error<User>(
                        error,
                        null
                    )
                )
            }

            override fun onHttpException(error: JsonElement) {
                loginGoogleResponse.setValue(
                    error<User>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(loginResponse: User) {
                loginGoogleResponse.setValue(
                    success(
                        loginResponse
                    )
                )
            }
        }
        loginGoogleUseCase.execute(disposable, LoginGoogleUseCase.Params.forGoogle(token))
    }

    fun getcheckEmailRegisteredResponse(): MutableLiveData<Resource<Boolean>> {
        return checkEmailRegisteredResponse
    }

    override fun onCleared() {
        super.onCleared()
        loginEmailUseCase.dispose()
        loginFacebookUseCase.dispose()
        checkIskEmailRegisteredUseCase.dispose()
    }

}