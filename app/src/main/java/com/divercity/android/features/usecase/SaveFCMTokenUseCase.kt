package com.divercity.android.features.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.device.body.Device
import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.data.entity.device.response.DeviceResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class SaveFCMTokenUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<DeviceResponse, SaveFCMTokenUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<DeviceResponse> {
        val deviceBody = DeviceBody(Device("android", true, params.token))
        return repository.saveDevice(deviceBody)
    }

    class Params private constructor(val token: String) {

        companion object {

            fun forDevice(token: String): Params {
                return Params(token)
            }
        }
    }
}
