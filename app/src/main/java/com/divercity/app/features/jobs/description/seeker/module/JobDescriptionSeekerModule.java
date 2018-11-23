package com.divercity.app.features.jobs.description.seeker.module;

import android.support.v4.app.FragmentManager;

import com.divercity.app.features.jobs.description.seeker.JobDescriptionSeekerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class JobDescriptionSeekerModule {

    @Provides
    static FragmentManager provideFragmentManager(JobDescriptionSeekerFragment fragment) {
        return fragment.getChildFragmentManager();
    }

}
