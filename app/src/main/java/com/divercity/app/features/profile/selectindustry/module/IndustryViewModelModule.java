package com.divercity.app.features.profile.selectindustry.module;

import com.divercity.app.features.profile.selectindustry.industry.IndustryPaginatedRepository;
import com.divercity.app.features.profile.selectindustry.industry.IndustryPaginatedRepositoryImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class IndustryViewModelModule {

    @Binds
    public abstract IndustryPaginatedRepository bindCompanyPaginatedRepository
            (IndustryPaginatedRepositoryImpl repository);

}
