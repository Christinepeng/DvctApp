package com.divercity.android.di.module.networking.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.divercity.android.BuildConfig
import com.divercity.android.data.networking.config.CheckConnectivityInterceptor
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.type.CustomType
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Created by lucas on 24/10/2018.
 */

@Module
class ApiApolloModule {

    @Provides
    @Singleton
    fun providesApolloClient(
        loggingInterceptor: HttpLoggingInterceptor,
        codeCheckConnectivityInterceptor: CheckConnectivityInterceptor,
        sessionRepository: SessionRepository
    ): ApolloClient {

        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(
                            original.method(),
                            original.body()
                    )
                    builder.addHeader("access-token", sessionRepository.getAccessToken()!!)
                    builder.addHeader("client", sessionRepository.getClient()!!)
                    builder.addHeader("uid", sessionRepository.getUid()!!)

                    chain.proceed(builder.build())
                }
                .addInterceptor(loggingInterceptor)
                .addInterceptor(codeCheckConnectivityInterceptor)
                .build()

        val customTypeAdapter = object : CustomTypeAdapter<String> {

            override fun encode(value: String): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(value)
            }

            override fun decode(value: CustomTypeValue<*>): String {
                return value.value.toString()
            }
        }

        return ApolloClient.builder()
                .serverUrl("https://".plus(BuildConfig.BASE_URL).plus("/graphql"))
                .okHttpClient(okHttp)
                .addCustomTypeAdapter(CustomType.ID, customTypeAdapter)
                .build()
    }
}