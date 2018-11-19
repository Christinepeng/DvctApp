package com.divercity.app.features.jobs.descriptionseeker.module;

import android.support.v4.app.FragmentManager;

import com.divercity.app.features.jobs.descriptionseeker.JobDescriptionSeekerFragment;

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
