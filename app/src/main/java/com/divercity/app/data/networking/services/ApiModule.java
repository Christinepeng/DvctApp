package com.divercity.app.data.networking.services;

import android.content.Context;

import com.divercity.app.BuildConfig;
import com.divercity.app.data.networking.config.ResponseCheckInterceptor;
import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    @Named("unauth")
    @Singleton
    Retrofit providesUnauthenticatedClient(ResponseCheckInterceptor responseCodeCheckInterceptor,
                                           HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor)
                .addInterceptor(responseCodeCheckInterceptor);

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
    }

    @Provides
    @Named("auth")
    @Singleton
    Retrofit providesAuthenticatedClient(ResponseCheckInterceptor responseCodeCheckInterceptor,
                                         HttpLoggingInterceptor loggingInterceptor,
                                         Interceptor interceptorHeader) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor)
                .addInterceptor(responseCodeCheckInterceptor)
                .addInterceptor(interceptorHeader);

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    ResponseCheckInterceptor providesConnectivityInterceptor(Context context) {
        return new ResponseCheckInterceptor(context);
    }

    @Provides
    Interceptor provideAuthorizationHeaders(final UserSharedPreferencesRepository sharedPreferencesRepository) {
        return chain -> {
            Request original = chain.request();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder();
            requestBuilder.addHeader("access-token", sharedPreferencesRepository.getAccessToken());
            requestBuilder.addHeader("client", sharedPreferencesRepository.getClient());
            requestBuilder.addHeader("uid", sharedPreferencesRepository.getUid());

            Request request = requestBuilder.build();

            return chain.proceed(request);
        };
    }

}
