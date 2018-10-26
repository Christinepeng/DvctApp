package com.divercity.app.di.module

import android.app.Application
import android.content.Context
import com.divercity.app.repository.feed.FeedRepository
import com.divercity.app.repository.feed.FeedRepositoryImpl
import com.divercity.app.repository.onboarding.OnboardingRepository
import com.divercity.app.repository.onboarding.OnboardingRepositoryImpl
import com.divercity.app.repository.registerlogin.RegisterLoginRepository
import com.divercity.app.repository.registerlogin.RegisterLoginRepositoryImpl
import com.divercity.app.repository.user.UserRepository
import com.divercity.app.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by lucas on 24/10/2018.
 */

@Module(includes = [AppProvidesModule::class])
abstract class AppBindsModule {

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindRegisterLoginRepository(repository: RegisterLoginRepositoryImpl): RegisterLoginRepository

    @Binds
    abstract fun bindFeedRepository(repository: FeedRepositoryImpl): FeedRepository

    @Binds
    abstract fun bindOnboardingRepository(repository: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    abstract fun bindContext(application: Application): Context
}