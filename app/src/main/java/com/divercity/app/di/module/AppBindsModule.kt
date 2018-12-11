package com.divercity.app.di.module

import android.app.Application
import android.content.Context
import com.divercity.app.repository.data.DataRepository
import com.divercity.app.repository.data.DataRepositoryImpl
import com.divercity.app.repository.document.DocumentRepository
import com.divercity.app.repository.document.DocumentRepositoryImpl
import com.divercity.app.repository.feed.FeedRepository
import com.divercity.app.repository.feed.FeedRepositoryImpl
import com.divercity.app.repository.job.JobRepository
import com.divercity.app.repository.job.JobRepositoryImpl
import com.divercity.app.repository.recommender.RecommenderRepository
import com.divercity.app.repository.recommender.RecommenderRepositoryImpl
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
    abstract fun bindDocumentRepository(repository: DocumentRepositoryImpl): DocumentRepository

    @Binds
    abstract fun bindOnboardingRepository(repository: DataRepositoryImpl): DataRepository

    @Binds
    abstract fun bindJobRepository(repository: JobRepositoryImpl): JobRepository

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindRecommenderRepository(repository: RecommenderRepositoryImpl): RecommenderRepository
}