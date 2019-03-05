package com.divercity.android.features.activity.module;

import com.divercity.android.features.activity.ActivityFragment;

import androidx.fragment.app.FragmentManager;
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
