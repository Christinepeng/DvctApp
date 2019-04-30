package com.divercity.android.di.module

import android.app.Application
import android.content.Context
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.chat.ChatRepositoryImpl
import com.divercity.android.repository.company.CompanyRepository
import com.divercity.android.repository.company.CompanyRepositoryImpl
import com.divercity.android.repository.data.DataRepository
import com.divercity.android.repository.data.DataRepositoryImpl
import com.divercity.android.repository.document.DocumentRepository
import com.divercity.android.repository.document.DocumentRepositoryImpl
import com.divercity.android.repository.group.GroupRepository
import com.divercity.android.repository.group.GroupRepositoryImpl
import com.divercity.android.repository.job.JobRepository
import com.divercity.android.repository.job.JobRepositoryImpl
import com.divercity.android.repository.registerlogin.RegisterLoginRepository
import com.divercity.android.repository.registerlogin.RegisterLoginRepositoryImpl
import com.divercity.android.repository.user.UserRepository
import com.divercity.android.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

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
    abstract fun bindDocumentRepository(repository: DocumentRepositoryImpl): DocumentRepository

    @Binds
    abstract fun bindOnboardingRepository(repository: DataRepositoryImpl): DataRepository

    @Binds
    abstract fun bindJobRepository(repository: JobRepositoryImpl): JobRepository

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    @Singleton
    abstract fun bindChatRepository(repository: ChatRepositoryImpl): ChatRepository

    @Binds
    abstract fun bindGroupRepository(repository: GroupRepositoryImpl): GroupRepository

    @Binds
    abstract fun bindCompanyRepository(repository: CompanyRepositoryImpl): CompanyRepository
}