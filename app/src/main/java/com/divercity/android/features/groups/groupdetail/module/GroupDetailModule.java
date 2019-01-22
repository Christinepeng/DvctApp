package com.divercity.android.features.groups.groupdetail.module;

import android.support.v4.app.FragmentManager;

import com.divercity.android.features.groups.groupdetail.GroupDetailFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class GroupDetailModule {

    @Provides
    static FragmentManager provideFragmentManager(GroupDetailFragment groupDetailFragment) {
        return groupDetailFragment.getChildFragmentManager();
    }
}
