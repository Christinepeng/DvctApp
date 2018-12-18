package com.divercity.app.features.profile.module;

import android.support.v4.app.FragmentManager;

import com.divercity.app.features.profile.ProfileFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class ProfileModule {

    @Provides
    static FragmentManager provideFragmentManager(ProfileFragment profileFragment) {
        return profileFragment.getChildFragmentManager();
    }
}
