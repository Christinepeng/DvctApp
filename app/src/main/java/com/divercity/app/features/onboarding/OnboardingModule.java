package com.divercity.app.features.onboarding;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 26/09/2018.
 */

@Module
public abstract class OnboardingModule {

//    @ActivityScoped
//    @Binds
//    abstract UseCase provideCheckIsEmailRegisteredUseCase(ChecIskEmailRegisteredUseCase useCase);

    @Provides
    static ViewPagerOnboardingAdapter provideViewPagerOnboardingAdapter(Context context) {
        return new ViewPagerOnboardingAdapter(context);
    }

}
