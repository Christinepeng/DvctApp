package com.divercity.app.data.networking.config

import android.content.Context
import com.divercity.app.R
import com.divercity.app.core.extension.networkInfo
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by lucas on 24/10/2018.
 */

class ResponseCheckInterceptor(var context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val netInfo = context.networkInfo
        if (!(netInfo != null && netInfo.isConnected)) {
            throw NoConnectivityException()
        }

        return chain.proceed(chain.request())
    }

    inner class NoConnectivityException : IOException() {

        override val message: String?
            get() = context.resources.getString(R.string.error_connection)
    }
}