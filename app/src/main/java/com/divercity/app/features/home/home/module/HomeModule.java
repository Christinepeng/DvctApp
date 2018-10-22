package com.divercity.app.features.home.home.module;

import com.divercity.app.features.home.home.featured.FeaturedAdapter;
import com.divercity.app.features.home.home.feed.adapter.HomeAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class HomeModule {

    @Provides
    static HomeAdapter provideHomeAdapter() {
        return new HomeAdapter();
    }

    @Provides
    static FeaturedAdapter provideFeaturedAdapter() {
        return new FeaturedAdapter();
    }

}
