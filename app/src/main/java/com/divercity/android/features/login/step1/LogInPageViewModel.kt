package com.divercity.android.features.login.step1

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
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

/**
 * Created by lucas on 26/09/2018.
 */
class LogInPageViewModel @Inject constructor(
    private val application: Application,
    private val loginEmailUseCase: LoginEmailUseCase,
    private val requestResetPasswordUseCase: RequestResetPasswordUseCase,
    private val checkIskEmailRegisteredUseCase: CheckIskEmailRegisteredUseCase,
    private val loginFacebookUseCase: LoginFacebookUseCase,
    private val loginGoogleUseCase: LoginGoogleUseCase
) :
    BaseViewModel() {
    val checkEmailRegisteredResponse = SingleLiveEvent<Resource<Boolean>>()
    val loginEmailResponse = SingleLiveEvent<Resource<User>>()
    val requestResetPasswordResponse = SingleLiveEvent<Resource<Unit>>()
    val loginFacebookResponse = SingleLiveEvent<Resource<User>>()
    val loginGoogleResponse = SingleLiveEvent<Resource<User>>()
    val navigateToSignUp = SingleLiveEvent<Any>()
    val navigateToLogin = SingleLiveEvent<Any>()

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

    fun loginEmail(email: String?, password: String?) {
        if (password != null && password != "") {
            loginEmailResponse.value = loading<User>(null)
            val callback = object : LoginEmailUseCase.Callback() {
                override fun onFail(error: String) {
                    loginEmailResponse.value = error<User>(error, null)
                }

                override fun onSuccess(response: User) {
                    loginEmailResponse.value = success(response)
                }
            }
            loginEmailUseCase.execute(callback, LoginEmailUseCase.Params.forLogin(email, password))
        } else {
            loginEmailResponse.setValue(
                error<User>(
                    application.resources.getString(R.string.insert_valid_password),
                    null
                )
            )
        }
    }

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
        loginGoogleUseCase.dispose()
        checkIskEmailRegisteredUseCase.dispose()
    }

}