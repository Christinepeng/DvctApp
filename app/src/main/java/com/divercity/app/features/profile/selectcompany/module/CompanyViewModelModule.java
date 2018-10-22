package com.divercity.app.features.profile.selectcompany.module;

import com.divercity.app.features.profile.selectcompany.company.CompanyPaginatedRepository;
import com.divercity.app.features.profile.selectcompany.company.CompanyPaginatedRepositoryImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class CompanyViewModelModule {

    @Binds
    public abstract CompanyPaginatedRepository bindCompanyPaginatedRepository
            (CompanyPaginatedRepositoryImpl repository);

}
