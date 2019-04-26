package com.divercity.android.features.login.step2

import android.app.Application
import androidx.lifecycle.LiveData
import com.divercity.android.R
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.features.login.step2.usecase.LoginUseCase
import com.divercity.android.model.user.User
import javax.inject.Inject

/**
 * Created by lucas on 25/09/2018.
 */

class LoginViewModel @Inject
internal constructor(
    private val application: Application,
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    val login = SingleLiveEvent<Resource<User>>()
    private var userEmail: String? = null

    fun login(password: String?) {
        if (password != null && password != "") {
            login.value = Resource.loading<User>(null)
            val callback = object : LoginUseCase.Callback() {
                override fun onFail(error: String) {
                    login.value = Resource.error<User>(error, null)
                }

                override fun onSuccess(response: User) {
                    login.value = Resource.success(response)
                }
            }
            loginUseCase.execute(callback, LoginUseCase.Params.forLogin(userEmail, password))
        } else {
            login.setValue(
                Resource.error<User>(
                    application.resources.getString(R.string.insert_valid_password),
                    null
                )
            )
        }
    }

    fun getLogin(): LiveData<Resource<User>> {
        return login
    }

    fun setUserEmail(userEmail: String) {
        this.userEmail = userEmail
    }

    override fun onCleared() {
        super.onCleared()
        loginUseCase.dispose()
    }
}
