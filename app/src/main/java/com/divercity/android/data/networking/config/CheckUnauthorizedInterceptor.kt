package com.divercity.android.data.networking.config

import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by lucas on 24/10/2018.
 */

class CheckUnauthorizedInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        if (response.code() == 401) {
            RxBus.publish(RxEvent.EventUnauthorizedUser(""))
        }

        return response
    }
}