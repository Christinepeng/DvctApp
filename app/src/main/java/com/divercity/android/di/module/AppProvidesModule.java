package com.divercity.android.di.module;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.divercity.android.db.dao.UserDao;
import com.divercity.android.features.apollo.ApolloRepository;
import com.divercity.android.helpers.NotificationHelper;
import com.divercity.android.repository.appstate.AppStateRepository;
import com.divercity.android.repository.appstate.AppStateRepositoryImpl;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.session.SessionRepositoryImpl;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lucas on 8/31/17.
 */

@Module
public abstract class AppProvidesModule {

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

    @Provides
    @Singleton
    static SessionRepository provideSessionRepository(Context context, UserDao userDao) {
        return new SessionRepositoryImpl(context, userDao);
    }

    @Provides
    @Singleton
    static AppStateRepository provideAppStateRepository(Context context) {
        return new AppStateRepositoryImpl(context);
    }

    @Provides
    @Singleton
    static ApolloRepository provideApolloRepository(ApolloClient client) {
        return new ApolloRepository(client);
    }

    @Provides
    @Singleton
    static NotificationHelper provideNotificationHelper(Context context) {
        return new NotificationHelper(context);
    }
}
