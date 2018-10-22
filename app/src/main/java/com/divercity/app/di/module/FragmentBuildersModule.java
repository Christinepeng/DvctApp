package com.divercity.app.di.module;

import com.divercity.app.features.home.home.HomeFragment;
import com.divercity.app.features.home.home.module.HomeModule;
import com.divercity.app.features.home.jobs.JobsFragment;
import com.divercity.app.features.home.jobs.JobsModule;
import com.divercity.app.features.login.LoginFragment;
import com.divercity.app.features.onboarding.OnboardingFragment;
import com.divercity.app.features.onboarding.OnboardingModule;
import com.divercity.app.features.profile.selectcompany.SelectCompanyFragment;
import com.divercity.app.features.profile.selectcompany.module.CompanyModule;
import com.divercity.app.features.profile.selectcountry.SelectCountryFragment;
import com.divercity.app.features.profile.selectethnicity.SelectEthnicityFragment;
import com.divercity.app.features.profile.selectgender.SelectGenderFragment;
import com.divercity.app.features.profile.selectindustry.SelectIndustryFragment;
import com.divercity.app.features.profile.selectindustry.module.IndustryModule;
import com.divercity.app.features.profile.selectusertype.SelectUserTypeFragment;
import com.divercity.app.features.signup.SignUpFragment;
import com.divercity.app.features.splash.SplashFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by lucas on 27/09/2018.
 */

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract SplashFragment contributeSplashFragment();

    @ContributesAndroidInjector()
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector(modules = OnboardingModule.class)
    abstract OnboardingFragment contributeOnboardingFragment();

    @ContributesAndroidInjector()
    abstract SignUpFragment contributeSignUpFragment();

    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector(modules = JobsModule.class)
    abstract JobsFragment contributeJobsFragment();

    @ContributesAndroidInjector()
    abstract SelectUserTypeFragment contributeUserTypeFragment();

    @ContributesAndroidInjector(modules = CompanyModule.class)
    abstract SelectCompanyFragment contributeSelectCompanyFragment();

    @ContributesAndroidInjector(modules = IndustryModule.class)
    abstract SelectIndustryFragment contributeSelectIndustryFragment();

    @ContributesAndroidInjector()
    abstract SelectCountryFragment contributeSelectCountryFragment();

    @ContributesAndroidInjector()
    abstract SelectEthnicityFragment contributeSelectEthnicityFragment();

    @ContributesAndroidInjector()
    abstract SelectGenderFragment contributeSelectGenderFragment();
}
