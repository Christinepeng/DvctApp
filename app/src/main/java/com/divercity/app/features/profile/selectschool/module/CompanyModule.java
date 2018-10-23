package com.divercity.app.features.profile.selectschool.module;

import com.divercity.app.features.profile.selectschool.adapter.CompanyAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class CompanyModule {

    @Provides
    static CompanyAdapter provideCompanyAdapter() {
        return new CompanyAdapter();
    }

}
