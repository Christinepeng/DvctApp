package com.divercity.app.features.profile.selectgroups.module;

import com.divercity.app.features.profile.selectgroups.group.GroupPaginatedRepository;
import com.divercity.app.features.profile.selectgroups.group.GroupPaginatedRepositoryImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class GroupViewModelModule {

    @Binds
    public abstract GroupPaginatedRepository bindCompanyPaginatedRepository
            (GroupPaginatedRepositoryImpl repository);

}
