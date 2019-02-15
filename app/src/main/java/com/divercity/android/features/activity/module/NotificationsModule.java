package com.divercity.android.features.activity.module;

import android.support.v4.app.FragmentManager;

import com.divercity.android.features.activity.ActivityFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class NotificationsModule {

    @Provides
    static FragmentManager provideFragmentManager(ActivityFragment activityFragment) {
        return activityFragment.getChildFragmentManager();
    }
}
