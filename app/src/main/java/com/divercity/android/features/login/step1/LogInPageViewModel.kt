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
import com.divercity.android.features.login.step1.usecase.ChecIskEmailRegisteredUseCase
import com.divercity.android.features.login.step1.usecase.ConnectLinkedInApiHelper
import com.divercity.android.features.login.step1.usecase.LoginFacebookUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

/**
 * Created by lucas on 26/09/2018.
 */
class LogInPageViewModel @Inject constructor(
    private val application: Application,
    private val checIskEmailRegisteredUseCase: ChecIskEmailRegisteredUseCase,
    private val linkedInApiHelper: ConnectLinkedInApiHelper,
    private val loginFacebookUseCase: LoginFacebookUseCase
) :
    BaseViewModel() {
    val isEmailRegistered = SingleLiveEvent<Resource<Boolean>>()
    val loginFacebookResponse = SingleLiveEvent<Resource<User>>()
    val navigateToSignUp = SingleLiveEvent<Any>()
    val navigateToLogin = SingleLiveEvent<Any>()

    fun checkIfEmailRegistered(email: String) {
        if (Util.isValidEmail(email.trim { it <= ' ' })) {
            isEmailRegistered.setValue(
                loading<Boolean>(
                    null
                )
            )
            var disposableObserver: DisposableObserver<Boolean> = object : DisposableObserver<Boolean>() {
                override fun onNext(r: Boolean) {
                    isEmailRegistered.setValue(
                        success(
                            r
                        )
                    )
                    if (r) navigateToLogin.call() else navigateToSignUp.call()
                }

                override fun onError(e: Throwable) {
                    isEmailRegistered.setValue(
                        error<Boolean>(
                            e.message!!,
                            null
                        )
                    )
                }

                override fun onComplete() {}
            }
            checIskEmailRegisteredUseCase.execute(
                disposableObserver,
                ChecIskEmailRegisteredUseCase.Params.forCheckEmail(email)
            )
        } else isEmailRegistered.setValue(
            error<Boolean>(
                application.resources.getString(R.string.insert_valid_email),
                null
            )
        )
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

//    fun loginGoogle(token: String?) { // TODO
//    }

    fun getIsEmailRegistered(): MutableLiveData<Resource<Boolean>> {
        return isEmailRegistered
    }

    override fun onCleared() {
        super.onCleared()
        loginFacebookUseCase.dispose()
        checIskEmailRegisteredUseCase.dispose()
    }

}