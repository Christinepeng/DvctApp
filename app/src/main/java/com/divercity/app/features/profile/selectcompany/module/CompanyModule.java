package com.divercity.app.features.profile.selectcompany.module;

import com.divercity.app.features.profile.selectcompany.adapter.CompanyAdapter;

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
