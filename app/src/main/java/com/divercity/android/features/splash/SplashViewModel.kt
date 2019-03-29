package com.divercity.android.features.splash

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.usecase.FetchUserDataUseCase
import com.divercity.android.repository.session.SessionRepository
import org.json.JSONObject
import javax.inject.Inject

class SplashViewModel @Inject
internal constructor(
    private val fetchUserDataUseCase: FetchUserDataUseCase,
    private val session: SessionRepository
) : BaseViewModel() {

    val fetchUserDataResponse = SingleLiveEvent<Resource<UserResponse>>()
    val navigateToSelectUserType = SingleLiveEvent<Any>()
    val navigateToHome = SingleLiveEvent<Any>()
    val navigateToEnterEmail = SingleLiveEvent<Any>()
    val navigateToGroupDetail = SingleLiveEvent<Int>()

    var deepLinkData: JSONObject? = null

    fun fetchCurrentUserDataToCheckUserTypeDefined() {

        fetchUserDataResponse.value = Resource.loading(null)
        val callback = object : FetchUserDataUseCase.Callback() {

            override fun onFail(error: String) {
                fetchUserDataResponse.value = Resource.error(error, null)
            }

            override fun onSuccess(response: UserResponse) {
                fetchUserDataResponse.value = Resource.success(response)
                session.saveUserData(response)

                if (response.userAttributes?.accountType == null)
                    navigateToSelectUserType.call()
                else {
                    if (deepLinkData != null) {
                        checkRoute(deepLinkData)
                    } else {
                        navigateToHome.call()
                    }
                }
            }
        }
        fetchUserDataUseCase.execute(
            callback,
            FetchUserDataUseCase.Params.forUserData(session.getUserId())
        )
    }

    fun checkRouteNoDeepLink() {
        if (session.isUserLogged()) {
            fetchCurrentUserDataToCheckUserTypeDefined()
        } else {
            navigateToEnterEmail.call()
        }
    }

    fun checkRouteDeepLink(data: JSONObject) {
        deepLinkData = data
        if (session.isUserLogged()) {
//            If the app is already open and user is logged it won't be null. If the app is closed
//            and user is logged it will be null and we have to check data on the server.
            if (session.getUserType() != null) {
                checkRoute(data)
            } else {
                fetchCurrentUserDataToCheckUserTypeDefined()
            }
        } else {
            navigateToEnterEmail.call()
        }
    }

    fun checkRoute(data: JSONObject?) {
        when (data?.getString("type")) {
            "group_invite" -> {
                navigateToGroupDetail.postValue(data.getInt("id"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchUserDataUseCase.dispose()
    }
}