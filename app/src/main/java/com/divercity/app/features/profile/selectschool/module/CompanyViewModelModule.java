package com.divercity.app.features.profile.selectschool.module;

import com.divercity.app.features.profile.selectschool.company.CompanyPaginatedRepository;
import com.divercity.app.features.profile.selectschool.company.CompanyPaginatedRepositoryImpl;

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
