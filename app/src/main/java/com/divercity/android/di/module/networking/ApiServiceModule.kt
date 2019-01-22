package com.divercity.android.di.module.networking

import com.divercity.android.data.networking.services.*
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
    fun provideOnboardingService(@Named("auth") retrofit: Retrofit): DataService {
        return retrofit.create(DataService::class.java)
    }

    @Provides
    @Singleton
    fun provideJobService(@Named("auth") retrofit: Retrofit): JobService {
        return retrofit.create(JobService::class.java)
    }

    @Provides
    @Singleton
    fun provideDocumentsService(@Named("auth") retrofit: Retrofit): DocumentService {
        return retrofit.create(DocumentService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatService(@Named("auth") retrofit: Retrofit): ChatService {
        return retrofit.create(ChatService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupService(@Named("auth") retrofit: Retrofit): GroupService {
        return retrofit.create(GroupService::class.java)
    }
}