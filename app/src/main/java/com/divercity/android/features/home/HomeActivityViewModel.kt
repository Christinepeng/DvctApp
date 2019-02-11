package com.divercity.android.features.home

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.entity.device.response.DeviceResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.usecase.SaveFCMTokenUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 01/11/2018.
 */

class HomeActivityViewModel @Inject
constructor(var sessionRepository: SessionRepository,
            private var saveFCMTokenUseCase: SaveFCMTokenUseCase) : BaseViewModel() {

    fun getProfilePictureUrl(): String? = sessionRepository.getUserAvatarUrl()

    fun checkFCMDevice(){
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        if(sessionRepository.getDeviceId() == null){
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                val callback = object : DisposableObserverWrapper<DeviceResponse>() {
                    override fun onFail(error: String) {
                    }

                    override fun onHttpException(error: JsonElement) {
                    }

                    override fun onSuccess(o: DeviceResponse) {
                        sessionRepository.setDeviceId(o.id)
                        sessionRepository.setFCMToken(o.attributes?.token)
                    }
                }
                saveFCMTokenUseCase.execute(callback, SaveFCMTokenUseCase.Params.forDevice(it.token))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        saveFCMTokenUseCase.dispose()
    }
}