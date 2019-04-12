package com.divercity.android.features.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.device.body.Device
import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class UpdateFCMTokenUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<Boolean, UpdateFCMTokenUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Boolean> {
        val deviceBody = DeviceBody(Device("android", params.enabled, params.token))
        return repository.updateDevice(params.deviceId, deviceBody)
    }

    class Params private constructor(val deviceId: String, val token: String, val enabled: Boolean) {

        companion object {

            fun forDevice(deviceId: String, token: String, enabled: Boolean): Params {
                return Params(deviceId, token, enabled)
            }
        }
    }
}
