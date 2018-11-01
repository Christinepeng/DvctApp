package com.divercity.app.features.linkedin

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.features.linkedin.usecase.LoginLinkedInUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/11/2018.
 */

class LinkedinViewModel @Inject
constructor(val loginLinkedInUseCase: LoginLinkedInUseCase) : BaseViewModel() {

    val loginLinkedInResponse = SingleLiveEvent<Resource<LoginResponse>>()
    val navigateToSelectUserType = SingleLiveEvent<Boolean>()
    val navigateToHome = SingleLiveEvent<Boolean>()

    fun loginLinkedin(code: String?, state: String?) {
        code?.let {
            state?.let {
                loginLinkedInResponse.value = Resource.loading(null)
                val callback = object : LoginLinkedInUseCase.Callback() {
                    override fun onFail(msg: String) {
                        loginLinkedInResponse.value = Resource.error(msg, null)
                    }

                    override fun onSuccess(response: LoginResponse) {
                        if (response.attributes?.accountType == null)
                            navigateToSelectUserType.setValue(true)
                        else
                            navigateToHome.setValue(true)
                    }
                }
                compositeDisposable.add(callback)
                loginLinkedInUseCase.execute(callback, LoginLinkedInUseCase.Params.forLogin(code, state))
            }
        }
    }
}