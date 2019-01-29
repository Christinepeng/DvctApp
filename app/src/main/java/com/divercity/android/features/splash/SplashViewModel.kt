package com.divercity.android.features.splash

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.usecase.FetchUserDataUseCase
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class SplashViewModel @Inject
internal constructor(
    private val fetchUserDataUseCase: FetchUserDataUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    val userData = SingleLiveEvent<Resource<UserResponse>>()
    val navigateToSelectUserType = SingleLiveEvent<Boolean>()
    val navigateToHome = SingleLiveEvent<Boolean>()

    val isUserLogged: Boolean
        get() = sessionRepository.isUserLogged()

    fun fetchCurrentUserDataToCheckUserTypeDefined() {

        userData.value = Resource.loading(null)
        val callback = object : FetchUserDataUseCase.Callback() {

            override fun onFail(error: String) {
                userData.value = Resource.error<UserResponse>(error, null)
            }

            override fun onSuccess(response: UserResponse) {
                userData.value = Resource.success(response)
                sessionRepository.saveUserData(response)

                if (response.userAttributes?.accountType == null)
                    navigateToSelectUserType.value = true
                else
                    navigateToHome.value = true
            }
        }
        compositeDisposable.add(callback)
        fetchUserDataUseCase.execute(callback, FetchUserDataUseCase.Params.forUserData(sessionRepository.getUserId()))
    }
}