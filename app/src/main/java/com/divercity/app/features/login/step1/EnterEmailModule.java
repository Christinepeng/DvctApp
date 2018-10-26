package com.divercity.app.features.login.step1;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 26/09/2018.
 */

@Module
public abstract class EnterEmailModule {

    @Provides
    static ViewPagerEnterEmailAdapter provideViewPagerOnboardingAdapter(Context context) {
        return new ViewPagerEnterEmailAdapter(context);
    }
}
