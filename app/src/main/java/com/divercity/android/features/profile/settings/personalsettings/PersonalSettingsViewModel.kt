package com.divercity.android.features.profile.settings.personalsettings

import android.app.Application
import com.divercity.android.R
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.usecase.UpdateFCMTokenUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class PersonalSettingsViewModel @Inject
constructor(
    private val context : Application,
    private val sessionRepository: SessionRepository,
    private val updateFCMTokenUseCase: UpdateFCMTokenUseCase
) : BaseViewModel() {

    val updateFCMTokenResponse = SingleLiveEvent<Resource<Boolean>>()
//    val usernameRegisteredResponse = SingleLiveEvent<Resource<Boolean>>()
//    val uploadProfilePictureResponse = SingleLiveEvent<Resource<UserResponse>>()
//    val navigateToSelectUserType = SingleLiveEvent<Boolean>()

    fun getProfilePicture(): String? {
        return sessionRepository.getUserAvatarUrl()
    }

    fun enableNotifications(enabled: Boolean) {
        updateFCMTokenResponse.postValue(Resource.loading(null))
        if (!sessionRepository.getDeviceId().isNullOrEmpty() && !sessionRepository.getFCMToken().isNullOrEmpty()) {
            val callback = object : DisposableObserverWrapper<Boolean>() {
                override fun onFail(error: String) {
                    updateFCMTokenResponse.postValue(Resource.error(error, enabled))
                }

                override fun onHttpException(error: JsonElement) {
                    updateFCMTokenResponse.postValue(Resource.error(error.toString(), enabled))

                }

                override fun onSuccess(o: Boolean) {
                    updateFCMTokenResponse.postValue(Resource.success(enabled))
                }
            }
            updateFCMTokenUseCase.execute(
                callback, UpdateFCMTokenUseCase.Params.forDevice(
                    sessionRepository.getDeviceId()!!,
                    sessionRepository.getFCMToken()!!,
                    enabled
                )
            )
        } else {
            updateFCMTokenResponse.postValue(Resource.error(context.resources.getString(R.string.error_notifications), null))
        }
    }
}
