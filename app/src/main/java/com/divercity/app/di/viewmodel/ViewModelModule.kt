package com.divercity.app.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.divercity.app.features.company.base.SelectCompanyViewModel
import com.divercity.app.features.groups.TabGroupsViewModel
import com.divercity.app.features.groups.all.AllGroupsViewModel
import com.divercity.app.features.groups.mygroups.MyGroupsViewModel
import com.divercity.app.features.groups.trending.TrendingGroupsViewModel
import com.divercity.app.features.home.HomeActivityViewModel
import com.divercity.app.features.home.home.HomeViewModel
import com.divercity.app.features.jobposting.JobPostingViewModel
import com.divercity.app.features.jobposting.jobtype.JobTypeViewModel
import com.divercity.app.features.jobposting.sharetogroup.ShareJobGroupViewModel
import com.divercity.app.features.jobposting.skills.JobSkillsViewModel
import com.divercity.app.features.jobs.TabJobsViewModel
import com.divercity.app.features.jobs.descriptionseeker.JobDescriptionSeekerViewModel
import com.divercity.app.features.jobs.jobs.JobsListViewModel
import com.divercity.app.features.jobs.mypostings.JobsMyPostingsViewModel
import com.divercity.app.features.jobs.saved.SavedJobsViewModel
import com.divercity.app.features.linkedin.LinkedinViewModel
import com.divercity.app.features.location.base.SelectLocationViewModel
import com.divercity.app.features.location.onboarding.OnboardingLocationViewModel
import com.divercity.app.features.login.step1.EnterEmailViewModel
import com.divercity.app.features.login.step2.LoginViewModel
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptViewModel
import com.divercity.app.features.onboarding.selectbirthdate.SelectBirthdayViewModel
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
    @ViewModelKey(TabJobsViewModel::class)
    abstract fun bindsJobsViewModel(viewModelTab: TabJobsViewModel): ViewModel

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
    @ViewModelKey(OnboardingLocationViewModel::class)
    abstract fun bindsSelectCountryViewModel(viewModel: OnboardingLocationViewModel): ViewModel

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
    @ViewModelKey(JobsMyPostingsViewModel::class)
    abstract fun bindsMyPostingsViewModel(viewModelJobs: JobsMyPostingsViewModel): ViewModel

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

    @Binds
    @IntoMap
    @ViewModelKey(JobTypeViewModel::class)
    abstract fun bindsJobTypeViewModel(viewModel: JobTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShareJobGroupViewModel::class)
    abstract fun bindsShareJobGroupViewModel(viewModel: ShareJobGroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobSkillsViewModel::class)
    abstract fun bindsJobSkillsViewModel(viewModel: JobSkillsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TabGroupsViewModel::class)
    abstract fun bindsGroupsViewModel(viewModelTab: TabGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllGroupsViewModel::class)
    abstract fun bindsAllGroupsViewModel(viewModel: AllGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrendingGroupsViewModel::class)
    abstract fun bindsTrendingGroupsViewModel(viewModel: TrendingGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyGroupsViewModel::class)
    abstract fun bindsMyGroupsViewModel(viewModel: MyGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobDescriptionSeekerViewModel::class)
    abstract fun bindsJobDescriptionSeekerViewModel(viewModel: JobDescriptionSeekerViewModel): ViewModel
}