package com.divercity.android.features.profile.module;

import androidx.fragment.app.FragmentManager;

import com.divercity.android.features.profile.ProfileFragment;

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
