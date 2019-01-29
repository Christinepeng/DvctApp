package com.divercity.android.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.divercity.android.features.agerange.onboarding.OnboardingAgeViewModel
import com.divercity.android.features.chat.chat.ChatViewModel
import com.divercity.android.features.chat.newchat.NewChatViewModel
import com.divercity.android.features.chat.recentchats.newrecentchats.ChatsViewModel
import com.divercity.android.features.company.base.SelectCompanyViewModel
import com.divercity.android.features.company.companysize.CompanySizesViewModel
import com.divercity.android.features.company.createcompany.CreateCompanyViewModel
import com.divercity.android.features.company.onboarding.OnboardingCompanyViewModel
import com.divercity.android.features.dialogs.jobapplication.JobApplicationDialogViewModel
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogViewModel
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogViewModel
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityViewModel
import com.divercity.android.features.gender.onboarding.OnboardingGenderViewModel
import com.divercity.android.features.groups.TabGroupsViewModel
import com.divercity.android.features.groups.all.AllGroupsViewModel
import com.divercity.android.features.groups.creategroup.step1.CreateGroupViewModel
import com.divercity.android.features.groups.creategroup.step3.GroupDescriptionViewModel
import com.divercity.android.features.groups.groupdetail.GroupDetailViewModel
import com.divercity.android.features.groups.groupdetail.about.TabAboutGroupDetailViewModel
import com.divercity.android.features.groups.groupdetail.conversation.GroupConversationViewModel
import com.divercity.android.features.groups.mygroups.MyGroupsViewModel
import com.divercity.android.features.groups.onboarding.SelectGroupViewModel
import com.divercity.android.features.groups.trending.TrendingGroupsViewModel
import com.divercity.android.features.home.HomeActivityViewModel
import com.divercity.android.features.home.home.HomeViewModel
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingViewModel
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryViewModel
import com.divercity.android.features.jobposting.JobPostingViewModel
import com.divercity.android.features.jobposting.jobtype.JobTypeViewModel
import com.divercity.android.features.jobposting.sharetogroup.ShareJobGroupViewModel
import com.divercity.android.features.jobposting.skills.JobSkillsViewModel
import com.divercity.android.features.jobs.TabJobsViewModel
import com.divercity.android.features.jobs.applicants.JobApplicantsViewModel
import com.divercity.android.features.jobs.applications.JobsApplicationsViewModel
import com.divercity.android.features.jobs.description.detail.JobDetailViewModel
import com.divercity.android.features.jobs.description.poster.JobDescriptionPosterViewModel
import com.divercity.android.features.jobs.jobs.JobsListViewModel
import com.divercity.android.features.jobs.mypostings.MyJobsPostingsViewModel
import com.divercity.android.features.jobs.savedjobs.SavedJobsViewModel
import com.divercity.android.features.jobs.similarjobs.SimilarJobListViewModel
import com.divercity.android.features.linkedin.LinkedinViewModel
import com.divercity.android.features.location.base.SelectLocationViewModel
import com.divercity.android.features.location.onboarding.OnboardingLocationViewModel
import com.divercity.android.features.login.step1.EnterEmailViewModel
import com.divercity.android.features.login.step2.LoginViewModel
import com.divercity.android.features.onboarding.profileprompt.ProfilePromptViewModel
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsViewModel
import com.divercity.android.features.onboarding.selectmajor.SelectMajorViewModel
import com.divercity.android.features.onboarding.selectoccupation.SelectOccupationViewModel
import com.divercity.android.features.onboarding.selectoccupationofinterests.SelectOOIViewModel
import com.divercity.android.features.onboarding.selectschool.SelectSchoolViewModel
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeViewModel
import com.divercity.android.features.profile.ProfileViewModel
import com.divercity.android.features.profile.settings.ProfileSettingsViewModel
import com.divercity.android.features.profile.settings.interests.InterestsViewModel
import com.divercity.android.features.profile.settings.personalsettings.PersonalSettingsViewModel
import com.divercity.android.features.profile.tabfollower.ConnectionsViewModel
import com.divercity.android.features.profile.tabfollowing.FollowingViewModel
import com.divercity.android.features.profile.tabgroups.FollowingGroupsViewModel
import com.divercity.android.features.profile.tabprofile.TabProfileViewModel
import com.divercity.android.features.signup.SignUpViewModel
import com.divercity.android.features.splash.SplashViewModel
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
    @ViewModelKey(OnboardingAgeViewModel::class)
    abstract fun bindsSelectBirthdayViewModel(viewModel: OnboardingAgeViewModel): ViewModel

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
    @ViewModelKey(OnboardingEthnicityViewModel::class)
    abstract fun bindsOnboardingEthnicityViewModel(viewModel: OnboardingEthnicityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingGenderViewModel::class)
    abstract fun bindsOnboardingGenderViewModel(viewModel: OnboardingGenderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectSingleIndustryViewModel::class)
    abstract fun bindsSelectSingleIndustryViewModel(viewModel: SelectSingleIndustryViewModel): ViewModel

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
    @ViewModelKey(MyJobsPostingsViewModel::class)
    abstract fun bindsMyPostingsViewModel(viewModelJobs: MyJobsPostingsViewModel): ViewModel

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
    @ViewModelKey(JobDetailViewModel::class)
    abstract fun bindsJobDescriptionSeekerViewModel(viewModel: JobDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(JobDescriptionPosterViewModel::class)
    abstract fun bindsJobDescriptionPosterViewModel(viewModel: JobDescriptionPosterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobsApplicationsViewModel::class)
    abstract fun bindsJobsApplicationsViewModel(viewModel: JobsApplicationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobApplicantsViewModel::class)
    abstract fun bindsJobApplicantsViewModel(viewModel: JobApplicantsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobApplyDialogViewModel::class)
    abstract fun bindsJobApplyDialogViewModel(viewModel: JobApplyDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecentDocsDialogViewModel::class)
    abstract fun bindsRecentDocsDialogViewModel(viewModel: RecentDocsDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateCompanyViewModel::class)
    abstract fun bindsCreateCompanyViewModel(viewModel: CreateCompanyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompanySizesViewModel::class)
    abstract fun bindsCompanySizesViewModel(viewModel: CompanySizesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SimilarJobListViewModel::class)
    abstract fun bindsSimilarJobListViewModel(viewModel: SimilarJobListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectOccupationViewModel::class)
    abstract fun bindsSelectOccupationViewModel(viewModel: SelectOccupationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectOOIViewModel::class)
    abstract fun bindsSelectOOIViewModel(viewModel: SelectOOIViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectInterestsViewModel::class)
    abstract fun bindsSelectInterestsViewModel(viewModel: SelectInterestsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingCompanyViewModel::class)
    abstract fun bindsOnboardingCompanyViewModel(viewModel: OnboardingCompanyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectIndustryOnboardingViewModel::class)
    abstract fun bindsSelectMultiIndustryViewModel(viewModel: SelectIndustryOnboardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindsProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConnectionsViewModel::class)
    abstract fun bindsFollowerViewModel(viewModel: ConnectionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TabProfileViewModel::class)
    abstract fun bindsTabProfileViewModel(viewModel: TabProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowingViewModel::class)
    abstract fun bindsFollowingViewModel(viewModel: FollowingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowingGroupsViewModel::class)
    abstract fun bindsFollowingGroupsViewModel(viewModel: FollowingGroupsViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(ChatsViewModel::class)
//    abstract fun bindsDirectMessagesViewModel(viewModel: com.divercity.android.features.chat.recentchats.oldrecentchats.ChatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewChatViewModel::class)
    abstract fun bindsNewChatViewModel(viewModel: NewChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobApplicationDialogViewModel::class)
    abstract fun bindsJobApplicationDialogViewModel(viewModel: JobApplicationDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindsChatViewModel(viewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateGroupViewModel::class)
    abstract fun bindsCreateGroupViewModel(viewModel: CreateGroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupDetailViewModel::class)
    abstract fun bindsGroupDetailViewModel(viewModel: GroupDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TabAboutGroupDetailViewModel::class)
    abstract fun bindsTabAboutGroupDetailViewModel(viewModel: TabAboutGroupDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupConversationViewModel::class)
    abstract fun bindsGroupConversationViewModel(viewModel: GroupConversationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupDescriptionViewModel::class)
    abstract fun bindsGroupDescriptionViewModel(viewModel: GroupDescriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileSettingsViewModel::class)
    abstract fun bindsProfileSettingsViewModel(viewModel: ProfileSettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalSettingsViewModel::class)
    abstract fun bindsPersonalSettingsViewModel(viewModel: PersonalSettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InterestsViewModel::class)
    abstract fun bindsInterestsViewModel(viewModel: InterestsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatsViewModel::class)
    abstract fun bindsChatsViewModel(viewModel: com.divercity.android.features.chat.recentchats.newrecentchats.ChatsViewModel): ViewModel
}