package com.divercity.android.features.home

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.entity.device.response.DeviceResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.usecase.SaveFCMTokenUseCase
import com.divercity.android.repository.user.UserRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 01/11/2018.
 */

class HomeActivityViewModel @Inject
constructor(var userRepository: UserRepository,
            private var saveFCMTokenUseCase: SaveFCMTokenUseCase) : BaseViewModel() {

    fun getProfilePictureUrl(): String? = userRepository.getAvatarThumbUrl()

    fun checkFCMDevice(){
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        if(userRepository.getDeviceId() == null){
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                val callback = object : DisposableObserverWrapper<DeviceResponse>() {
                    override fun onFail(error: String) {
                    }

                    override fun onHttpException(error: JsonElement) {
                    }

                    override fun onSuccess(o: DeviceResponse) {
                        userRepository.setDeviceId(o.id)
                        userRepository.setFCMToken(o.attributes?.token)
                    }
                }
                compositeDisposable.add(callback)
                saveFCMTokenUseCase.execute(callback, SaveFCMTokenUseCase.Params.forDevice(it.token))
            }
        }
    }
}