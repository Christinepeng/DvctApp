package com.divercity.app.di.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.divercity.app.features.home.home.HomeViewModel;
import com.divercity.app.features.home.home.module.HomeViewModelModule;
import com.divercity.app.features.home.jobs.JobsViewModel;
import com.divercity.app.features.login.LoginViewModel;
import com.divercity.app.features.onboarding.OnboardingViewModel;
import com.divercity.app.features.profile.selectcompany.SelectCompanyViewModel;
import com.divercity.app.features.profile.selectcompany.module.CompanyViewModelModule;
import com.divercity.app.features.profile.selectcountry.SelectCountryViewModel;
import com.divercity.app.features.profile.selectethnicity.SelectEthnicityViewModel;
import com.divercity.app.features.profile.selectgender.SelectGenderViewModel;
import com.divercity.app.features.profile.selectindustry.SelectIndustryViewModel;
import com.divercity.app.features.profile.selectindustry.module.IndustryViewModelModule;
import com.divercity.app.features.profile.selectusertype.SelectUserTypeViewModel;
import com.divercity.app.features.signup.SignUpViewModel;
import com.divercity.app.features.splash.SplashViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by lucas on 25/09/2018.
 */
@Module(includes = {
        HomeViewModelModule.class,
        CompanyViewModelModule.class,
        IndustryViewModelModule.class})
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel bindsSplashViewModel(SplashViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindsMovieDetailViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingViewModel.class)
    abstract ViewModel bindsOnboardingViewModel(OnboardingViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel.class)
    abstract ViewModel bindsSignUpViewModel(SignUpViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindsHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(JobsViewModel.class)
    abstract ViewModel bindsJobsViewModel(JobsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectUserTypeViewModel.class)
    abstract ViewModel bindsUserTypeViewModel(SelectUserTypeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectCompanyViewModel.class)
    abstract ViewModel bindsSelectCompanyViewModel(SelectCompanyViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectIndustryViewModel.class)
    abstract ViewModel bindsSelectIndustryViewModel(SelectIndustryViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectCountryViewModel.class)
    abstract ViewModel bindsSelectCountryViewModel(SelectCountryViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectEthnicityViewModel.class)
    abstract ViewModel bindsSelectEthnicityViewModel(SelectEthnicityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectGenderViewModel.class)
    abstract ViewModel bindsSelectGenderViewModel(SelectGenderViewModel viewModel);

    @Binds
    public abstract ViewModelProvider.Factory bindsViewModelFactory(DivercityViewModelFactory divercityViewModelFactory);

}
