package com.divercity.app.features.splash

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.features.profile.usecase.FetchUserDataUseCase
import com.divercity.app.repository.user.LoggedUserRepositoryImpl

import javax.inject.Inject

class SplashViewModel @Inject
internal constructor(
        private val fetchUserDataUseCase: FetchUserDataUseCase,
        private val loggedUserRepository: LoggedUserRepositoryImpl
) : BaseViewModel() {

    val userData = SingleLiveEvent<Resource<LoginResponse>>()
    val navigateToSelectUserType = SingleLiveEvent<Boolean>()
    val navigateToHome = SingleLiveEvent<Boolean>()

    val isUserLogged: Boolean
        get() = loggedUserRepository.isUserLogged()

    fun fetchCurrentUserDataToCheckUserTypeDefined() {

        userData.value = Resource.loading(null)
        val callback = object : FetchUserDataUseCase.Callback() {

            override fun onFail(error: String) {
                userData.value = Resource.error<LoginResponse>(error, null)
            }

            override fun onSuccess(response: LoginResponse) {
                userData.value = Resource.success(response)
                if (response.attributes?.accountType == null)
                    navigateToSelectUserType.value = true
                else
                    navigateToHome.value = true
            }
        }
        compositeDisposable.add(callback)
        fetchUserDataUseCase.execute(callback, FetchUserDataUseCase.Params.forUserData(loggedUserRepository.getUserId()))
    }
}