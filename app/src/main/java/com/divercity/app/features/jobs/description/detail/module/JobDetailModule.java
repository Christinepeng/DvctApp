package com.divercity.app.features.jobs.description.detail.module;

import android.support.v4.app.FragmentManager;

import com.divercity.app.features.jobs.description.detail.JobDetailFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class JobDetailModule {

    @Provides
    static FragmentManager provideFragmentManager(JobDetailFragment fragment) {
        return fragment.getChildFragmentManager();
    }

}
