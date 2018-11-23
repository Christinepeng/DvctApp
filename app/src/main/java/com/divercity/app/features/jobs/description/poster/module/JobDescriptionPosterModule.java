package com.divercity.app.features.jobs.description.poster.module;

import android.support.v4.app.FragmentManager;

import com.divercity.app.features.jobs.description.poster.JobDescriptionPosterFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class JobDescriptionPosterModule {

    @Provides
    static FragmentManager provideFragmentManager(JobDescriptionPosterFragment fragment) {
        return fragment.getChildFragmentManager();
    }

}
