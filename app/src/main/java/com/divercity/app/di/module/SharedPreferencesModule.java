package com.divercity.app.di.module;

import android.content.Context;

import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 28/09/2018.
 */

@Module
public abstract class SharedPreferencesModule {

    @Provides
    @Singleton
    static UserSharedPreferencesRepository provideUserSharedPreferenceRepository
            (Context context) {
        return new UserSharedPreferencesRepository(context);
    }
}
