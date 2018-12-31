package com.divercity.app.di.module.networking

import android.content.Context
import com.divercity.app.BuildConfig
import com.divercity.app.data.networking.config.ResponseCheckInterceptor
import com.divercity.app.repository.user.LoggedUserRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
        responseCodeCheckInterceptor: ResponseCheckInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("addApiPath") addApiPath : Interceptor
    ): Retrofit {

        val httpClient = OkHttpClient.Builder()
        httpClient
            .addInterceptor(loggingInterceptor)
            .addInterceptor(responseCodeCheckInterceptor)
//            .addInterceptor(addApiPath)

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
        responseCodeCheckInterceptor: ResponseCheckInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        interceptorHeader: Interceptor,
        @Named("addApiPath") addApiPath : Interceptor
    ): Retrofit {

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(loggingInterceptor)
            .addInterceptor(responseCodeCheckInterceptor)
            .addInterceptor(interceptorHeader)
//            .addInterceptor(addApiPath)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun providesConnectivityInterceptor(context: Context): ResponseCheckInterceptor {
        return ResponseCheckInterceptor(context)
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
    @Named("addApiPath")
    internal fun addApiPath(): Interceptor {
        return Interceptor {
            val original = it.request()

            // Request customization: add request headers
            val originalHttpUrl = original.url()

           val url = originalHttpUrl
               .newBuilder()
               .addPathSegment("api")
               .build()

            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            it.proceed(request)
        }
    }
}