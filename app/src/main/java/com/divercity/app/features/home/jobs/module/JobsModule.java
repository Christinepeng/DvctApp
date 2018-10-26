package com.divercity.app.features.home.jobs.module;

import android.support.v4.app.FragmentManager;

import com.divercity.app.features.home.jobs.JobsFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class JobsModule {

    @Provides
    static FragmentManager provideFragmentManager(JobsFragment jobsFragment) {
        return jobsFragment.getActivity().getSupportFragmentManager();
    }

}
