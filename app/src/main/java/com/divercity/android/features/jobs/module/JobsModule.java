package com.divercity.android.features.jobs.module;

import android.support.v4.app.FragmentManager;

import com.divercity.android.features.jobs.TabJobsFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class JobsModule {

    @Provides
    static FragmentManager provideFragmentManager(TabJobsFragment tabJobsFragment) {
        return tabJobsFragment.getChildFragmentManager();
    }

}
