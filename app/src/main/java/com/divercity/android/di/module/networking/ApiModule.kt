package com.divercity.android.di.module.networking

import android.content.Context
import com.divercity.android.BuildConfig
import com.divercity.android.core.utils.MySocket
import com.divercity.android.data.networking.config.CheckConnectivityInterceptor
import com.divercity.android.data.networking.config.CheckUnauthorizedInterceptor
import com.divercity.android.repository.user.LoggedUserRepositoryImpl
import com.divercity.android.socket.ChatWebSocket
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by lucas on 24/10/2018.
 */

@Module(includes = [ApiServiceModule::class])
class ApiModule {

    companion object {
        const val BASE_URL = "https://".plus(BuildConfig.BASE_URL).plus("/api/")
    }

    @Provides
    @Named("unauth")
    @Singleton
    fun providesUnauthenticatedClient(
        checkConnectivityInterceptor: CheckConnectivityInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): Retrofit {

        val httpClient = OkHttpClient.Builder()
        httpClient
            .addInterceptor(loggingInterceptor)
            .addInterceptor(checkConnectivityInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Named("auth")
    @Singleton
    fun providesAuthenticatedClient(
        checkConnectivityInterceptor: CheckConnectivityInterceptor,
        checkUnauthorizedInterceptor: CheckUnauthorizedInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        interceptorHeader: Interceptor
    ): Retrofit {

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(loggingInterceptor)
            .addInterceptor(checkConnectivityInterceptor)
            .addInterceptor(checkUnauthorizedInterceptor)
            .addInterceptor(interceptorHeader)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(addApiPath)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    @Singleton
    fun providesConnectivityInterceptor(context: Context): CheckConnectivityInterceptor {
        return CheckConnectivityInterceptor(context)
    }

    @Provides
    @Singleton
    fun providesUnauthorizedInterceptor(): CheckUnauthorizedInterceptor {
        return CheckUnauthorizedInterceptor()
    }

    @Provides
    internal fun provideAuthorizationHeaders(userLocalDataStore: LoggedUserRepositoryImpl): Interceptor {
        return Interceptor {
            val original = it.request()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()

            requestBuilder.addHeader("access-token", userLocalDataStore.getAccessToken()!!)
            requestBuilder.addHeader("client", userLocalDataStore.getClient()!!)
            requestBuilder.addHeader("uid", userLocalDataStore.getUid()!!)

            val request = requestBuilder.build()

            it.proceed(request)
        }
    }

    @Provides
    internal fun provideWebSocket(
        userLocalDataStore: LoggedUserRepositoryImpl,
        loggingInterceptor: HttpLoggingInterceptor
    ): MySocket {
        val url = "wss://".plus(BuildConfig.BASE_URL).plus("/cable")
            .plus("?")
            .plus("token=").plus(userLocalDataStore.getAccessToken()).plus("&")
            .plus("client=").plus(userLocalDataStore.getClient()).plus("&")
            .plus("uid=").plus(userLocalDataStore.getUid())

        return MySocket.Builder
            .with(url)
            .addHeader("origin", "https://www.divercity.io")
            .addHttpLogginInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    internal fun provideChatWebSocket(socket: MySocket): ChatWebSocket {
        return ChatWebSocket(socket)
    }
}