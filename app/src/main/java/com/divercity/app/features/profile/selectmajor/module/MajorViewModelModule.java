package com.divercity.app.features.profile.selectmajor.module;

import com.divercity.app.features.profile.selectmajor.major.MajorPaginatedRepository;
import com.divercity.app.features.profile.selectmajor.major.MajorPaginatedRepositoryImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class MajorViewModelModule {

    @Binds
    public abstract MajorPaginatedRepository bindCompanyPaginatedRepository
            (MajorPaginatedRepositoryImpl repository);

}
