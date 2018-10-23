package com.divercity.app.features.profile.selectgroups.module;

import com.divercity.app.features.profile.selectgroups.adapter.GroupsAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class GroupModule {

    @Provides
    static GroupsAdapter provideCompanyAdapter() {
        return new GroupsAdapter();
    }

}
