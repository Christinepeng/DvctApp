package com.divercity.app.di.module;

import android.app.Application;
import android.content.Context;

import com.divercity.app.repository.feed.FeedRepository;
import com.divercity.app.repository.feed.FeedRepositoryImpl;
import com.divercity.app.repository.registerlogin.RegisterLoginRepository;
import com.divercity.app.repository.registerlogin.RegisterLoginRepositoryImpl;
import com.divercity.app.repository.user.UserRepository;
import com.divercity.app.repository.user.UserRepositoryImpl;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lucas on 8/31/17.
 */

@Module
public abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);

    @Binds
    abstract RegisterLoginRepository bindRegisterLoginRepository(RegisterLoginRepositoryImpl repository);

    @Binds
    abstract FeedRepository bindFeedRepository(FeedRepositoryImpl repository);

    @Binds
    abstract UserRepository bindUserRepository(UserRepositoryImpl repository);

    @Provides
    @Named("executor_thread")
    @Singleton
    static Scheduler provideExecutorThread() {
        return Schedulers.io();
    }

    @Provides
    @Named("ui_thread")
    @Singleton
    static Scheduler provideUiThread() {
        return AndroidSchedulers.mainThread();
    }

}
