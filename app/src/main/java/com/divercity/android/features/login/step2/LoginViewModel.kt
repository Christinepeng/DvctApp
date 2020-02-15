package com.divercity.android.features.login.step2

import android.app.Application
import com.divercity.android.R
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.login.step1.usecase.LoginEmailUseCase
import com.divercity.android.features.login.step1.usecase.RequestResetPasswordUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 25/09/2018.
 */

class LoginViewModel @Inject
internal constructor(
    private val application: Application,
    private val loginEmailUseCase: LoginEmailUseCase,
    private val requestResetPasswordUseCase: RequestResetPasswordUseCase
) : BaseViewModel() {

    val login = SingleLiveEvent<Resource<User>>()
    val requestResetPasswordResponse = SingleLiveEvent<Resource<Unit>>()

    private lateinit var userEmail: String

    fun login(password: String?) {
        if (password != null && password != "") {
            login.value = Resource.loading<User>(null)
            val callback = object : LoginEmailUseCase.Callback() {
                override fun onFail(error: String) {
                    login.value = Resource.error<User>(error, null)
                }

                override fun onSuccess(response: User) {
                    login.value = Resource.success(response)
                }
            }
            loginEmailUseCase.execute(callback, LoginEmailUseCase.Params.forLogin(userEmail, password))
        } else {
            login.setValue(
                Resource.error<User>(
                    application.resources.getString(R.string.insert_valid_password),
                    null
                )
            )
        }
    }

    fun requestResetPassword() {
        requestResetPasswordResponse.value = Resource.loading(null)
        val callback = object : DisposableObserverWrapper<Unit>() {

            override fun onFail(error: String) {
                requestResetPasswordResponse.value = Resource.error(error, null)
            }

            override fun onHttpException(error: JsonElement) {
                requestResetPasswordResponse.value = Resource.error(error.toString(), null)
            }

            override fun onSuccess(t: Unit) {
                requestResetPasswordResponse.value = Resource.success(t)
            }
        }
        requestResetPasswordUseCase.execute(callback, RequestResetPasswordUseCase.Params.to(userEmail))
    }

    fun setUserEmail(userEmail: String) {
        this.userEmail = userEmail
    }

    override fun onCleared() {
        super.onCleared()
        loginEmailUseCase.dispose()
    }
}
