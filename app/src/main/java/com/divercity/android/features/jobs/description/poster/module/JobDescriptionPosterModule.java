package com.divercity.android.features.jobs.description.poster.module;

import androidx.fragment.app.FragmentManager;

import com.divercity.android.features.jobs.description.poster.JobDescriptionPosterFragment;

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
