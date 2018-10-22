package com.divercity.app.features.profile.selectindustry.module;

import com.divercity.app.features.profile.selectindustry.adapter.IndustryAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class IndustryModule {

    @Provides
    static IndustryAdapter provideCompanyAdapter() {
        return new IndustryAdapter();
    }

}
