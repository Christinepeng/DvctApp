package com.divercity.android.features.linkedin

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.linkedin.usecase.LoginLinkedInUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/11/2018.
 */

class LinkedinViewModel @Inject
constructor(val loginLinkedInUseCase: LoginLinkedInUseCase) : BaseViewModel() {

    val loginLinkedInResponse = SingleLiveEvent<Resource<UserResponse>>()
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

                    override fun onSuccess(response: UserResponse) {
                        if (response.userAttributes?.accountType == null)
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