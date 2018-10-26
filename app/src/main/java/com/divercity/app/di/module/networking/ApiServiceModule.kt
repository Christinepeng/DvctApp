package com.divercity.app.di.module.networking

import com.divercity.app.data.networking.services.FeedService
import com.divercity.app.data.networking.services.OnboardingService
import com.divercity.app.data.networking.services.RegisterLoginService
import com.divercity.app.data.networking.services.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by lucas on 24/10/2018.
 */

@Module
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideLoginService(@Named("unauth") retrofit: Retrofit): RegisterLoginService {
        return retrofit.create(RegisterLoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideInterestService(@Named("auth") retrofit: Retrofit): FeedService {
        return retrofit.create(FeedService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(@Named("auth") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideOnboardingService(@Named("auth") retrofit: Retrofit): OnboardingService {
        return retrofit.create(OnboardingService::class.java)
    }
}