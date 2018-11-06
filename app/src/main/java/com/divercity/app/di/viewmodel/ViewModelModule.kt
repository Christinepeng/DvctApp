package com.divercity.app.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.divercity.app.features.home.HomeActivityViewModel
import com.divercity.app.features.home.home.HomeViewModel
import com.divercity.app.features.home.jobs.JobsViewModel
import com.divercity.app.features.home.jobs.jobs.JobsListViewModel
import com.divercity.app.features.home.jobs.mypostings.MyPostingsViewModel
import com.divercity.app.features.home.jobs.saved.SavedJobsViewModel
import com.divercity.app.features.jobposting.JobPostingViewModel
import com.divercity.app.features.linkedin.LinkedinViewModel
import com.divercity.app.features.location.SelectLocationViewModel
import com.divercity.app.features.login.step1.EnterEmailViewModel
import com.divercity.app.features.login.step2.LoginViewModel
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptViewModel
import com.divercity.app.features.onboarding.selectbirthdate.SelectBirthdayViewModel
import com.divercity.app.features.onboarding.selectcompany.SelectCompanyViewModel
import com.divercity.app.features.onboarding.selectcountry.SelectCountryViewModel
import com.divercity.app.features.onboarding.selectethnicity.SelectEthnicityViewModel
import com.divercity.app.features.onboarding.selectgender.SelectGenderViewModel
import com.divercity.app.features.onboarding.selectgroups.SelectGroupViewModel
import com.divercity.app.features.onboarding.selectindustry.SelectIndustryViewModel
import com.divercity.app.features.onboarding.selectmajor.SelectMajorViewModel
import com.divercity.app.features.onboarding.selectschool.SelectSchoolViewModel
import com.divercity.app.features.onboarding.selectusertype.SelectUserTypeViewModel
import com.divercity.app.features.signup.SignUpViewModel
import com.divercity.app.features.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindsSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EnterEmailViewModel::class)
    abstract fun bindsEnterEmailViewModel(viewModel: EnterEmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindsSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindsLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobsViewModel::class)
    abstract fun bindsJobsViewModel(viewModel: JobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfilePromptViewModel::class)
    abstract fun bindsProfilePromptViewModel(viewModel: ProfilePromptViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectUserTypeViewModel::class)
    abstract fun bindsSelectUserTypeViewModel(viewModel: SelectUserTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectSchoolViewModel::class)
    abstract fun bindsSelectSchoolViewModel(viewModel: SelectSchoolViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectGroupViewModel::class)
    abstract fun bindsSelectGroupViewModel(viewModel: SelectGroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectBirthdayViewModel::class)
    abstract fun bindsSelectBirthdayViewModel(viewModel: SelectBirthdayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectCompanyViewModel::class)
    abstract fun bindsSelectCompanyViewModel(viewModel: SelectCompanyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectCountryViewModel::class)
    abstract fun bindsSelectCountryViewModel(viewModel: SelectCountryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectEthnicityViewModel::class)
    abstract fun bindsSelectEthnicityViewModel(viewModel: SelectEthnicityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectGenderViewModel::class)
    abstract fun bindsSelectGenderViewModel(viewModel: SelectGenderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectIndustryViewModel::class)
    abstract fun bindsSelectIndustryViewModel(viewModel: SelectIndustryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectMajorViewModel::class)
    abstract fun bindsSelectMajorViewModel(viewModel: SelectMajorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobsListViewModel::class)
    abstract fun bindsJobsListViewModel(viewModel: JobsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyPostingsViewModel::class)
    abstract fun bindsMyPostingsViewModel(viewModel: MyPostingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    abstract fun bindsHomeActivityViewModel(viewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LinkedinViewModel::class)
    abstract fun bindsLinkedinViewModel(viewModel: LinkedinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedJobsViewModel::class)
    abstract fun bindsJobsSavedViewModel(jobsViewModel: SavedJobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobPostingViewModel::class)
    abstract fun bindsJobPostingViewModel(viewModel: JobPostingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectLocationViewModel::class)
    abstract fun bindsSelectLocationViewModel(viewModel: SelectLocationViewModel): ViewModel
}