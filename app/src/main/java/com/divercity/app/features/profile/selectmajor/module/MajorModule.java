package com.divercity.app.features.profile.selectmajor.module;

import com.divercity.app.features.profile.selectmajor.adapter.MajorAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class MajorModule {

    @Provides
    static MajorAdapter provideCompanyAdapter() {
        return new MajorAdapter();
    }

}
