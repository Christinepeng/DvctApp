package com.divercity.app.di.module.networking.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.divercity.app.data.networking.config.ResponseCheckInterceptor
import com.divercity.app.type.CustomType
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
            responseCodeCheckInterceptor: ResponseCheckInterceptor
    ): ApolloClient {

        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(
                            original.method(),
                            original.body()
                    )
                    builder.addHeader(
                            "Authorization"
                            , "Bearer " + "9f1c0897e8996587d2c5264766b4cfd0d3178843"
                    )
                    chain.proceed(builder.build())
                }
                .addInterceptor(loggingInterceptor)
                .addInterceptor(responseCodeCheckInterceptor)
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
                .serverUrl("https://api.github.com/graphql")
                .okHttpClient(okHttp)
                .addCustomTypeAdapter(CustomType.URI, customTypeAdapter)
                .build()
    }
}