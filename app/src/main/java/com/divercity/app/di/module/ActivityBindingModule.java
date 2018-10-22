package com.divercity.app.di.module;

import com.divercity.app.features.home.HomeActivity;
import com.divercity.app.features.onboarding.OnboardingActivity;
import com.divercity.app.features.profile.selectcompany.SelectCompanyActivity;
import com.divercity.app.features.profile.selectindustry.SelectIndustryActivity;
import com.divercity.app.features.profile.selectusertype.SelectUserTypeActivity;
import com.divercity.app.features.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract SplashActivity initActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract HomeActivity homeActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract OnboardingActivity onboardingActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract SelectUserTypeActivity userTypeActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract SelectCompanyActivity selectCompanyActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract SelectIndustryActivity selectIndustryActivity();
}
