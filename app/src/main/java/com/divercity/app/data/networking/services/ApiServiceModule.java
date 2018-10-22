package com.divercity.app.data.networking.services;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by lucas on 07/09/2018.
 */

@Module
public class ApiServiceModule {

    @Provides
    @Singleton
    RegisterLoginService provideLoginService(@Named("unauth") Retrofit retrofit) {
        return retrofit.create(RegisterLoginService.class);
    }

    @Provides
    @Singleton
    FeedService provideInterestService(@Named("auth") Retrofit retrofit) {
        return retrofit.create(FeedService.class);
    }

    @Provides
    @Singleton
    UserService provideSessionService(@Named("auth") Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

}
