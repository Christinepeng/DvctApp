package com.divercity.android.features.groups.module;

import androidx.fragment.app.FragmentManager;

import com.divercity.android.features.groups.TabGroupsFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class GroupsModule {

    @Provides
    static FragmentManager provideFragmentManager(TabGroupsFragment tabGroupsFragment) {
        return tabGroupsFragment.getChildFragmentManager();
    }

}
