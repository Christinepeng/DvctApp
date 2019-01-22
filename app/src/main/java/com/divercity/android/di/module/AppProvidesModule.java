package com.divercity.android.di.module;

import android.content.Context;
import com.apollographql.apollo.ApolloClient;
import com.divercity.android.Session;
import com.divercity.android.features.apollo.ApolloRepository;
import com.divercity.android.helpers.NotificationHelper;
import com.divercity.android.repository.chat.ChatRepositoryImpl;
import com.divercity.android.repository.user.LoggedUserRepositoryImpl;
import com.divercity.android.repository.user.UserRepository;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Named;
import javax.inject.Singleton;

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
    static LoggedUserRepositoryImpl provideUserLocalDataStore(Context context) {
        return new LoggedUserRepositoryImpl(context);
    }

    @Provides
    @Singleton
    static ApolloRepository provideApolloRepository(ApolloClient client) {
        return new ApolloRepository(client);
    }

    @Provides
    @Singleton
    static Session provideSession(ChatRepositoryImpl chatRepositoryImpl,
                                  UserRepository userRepository) {
        return new Session(chatRepositoryImpl, userRepository);
    }

    @Provides
    @Singleton
    static NotificationHelper provideNotificationHelper(Context context) {
        return new NotificationHelper(context);
    }
}
