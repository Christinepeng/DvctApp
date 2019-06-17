package com.divercity.android.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.divercity.android.features.activity.TabActivityViewModel
import com.divercity.android.features.activity.connectionrequests.ConnectionRequestsViewModel
import com.divercity.android.features.activity.notifications.NotificationsViewModel
import com.divercity.android.features.agerange.onboarding.OnboardingAgeViewModel
import com.divercity.android.features.chat.chat.ChatViewModel
import com.divercity.android.features.chat.creategroupchat.CreateGroupChatViewModel
import com.divercity.android.features.chat.newchat.NewChatViewModel
import com.divercity.android.features.chat.newgroupchat.NewGroupChatViewModel
import com.divercity.android.features.chat.recentchats.oldrecentchats.ChatsViewModel
import com.divercity.android.features.company.companies.CompaniesViewModel
import com.divercity.android.features.company.companiesmycompanies.CompaniesMyCompaniesViewModel
import com.divercity.android.features.company.companyaddadmin.CompanyAddAdminViewModel
import com.divercity.android.features.company.companyadmin.CompanyAdminViewModel
import com.divercity.android.features.company.companydetail.CompanyDetailViewModel
import com.divercity.android.features.company.companydetail.employees.EmployeesViewModel
import com.divercity.android.features.company.companydetail.jobpostings.JobPostingsByCompanyViewModel
import com.divercity.android.features.company.companysize.CompanySizesViewModel
import com.divercity.android.features.company.createcompany.CreateCompanyViewModel
import com.divercity.android.features.company.deleteadmincompany.DeleteCompanyAdminViewModel
import com.divercity.android.features.company.diversityrating.DiversityRatingViewModel
import com.divercity.android.features.company.mycompanies.MyCompaniesViewModel
import com.divercity.android.features.company.ratecompany.RateCompanyViewModel
import com.divercity.android.features.company.selectcompany.base.SelectCompanyViewModel
import com.divercity.android.features.company.selectcompany.onboarding.OnboardingCompanyViewModel
import com.divercity.android.features.dialogs.jobapplication.JobApplicationDialogViewModel
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogViewModel
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogViewModel
import com.divercity.android.features.dialogs.ratecompany.RateCompanyDiversityDialogViewModel
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogViewModel
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityViewModel
import com.divercity.android.features.gender.onboarding.OnboardingGenderViewModel
import com.divercity.android.features.groups.TabGroupsViewModel
import com.divercity.android.features.groups.allgroups.AllGroupsViewModel
import com.divercity.android.features.groups.createeditgroup.step1.CreateEditGroupStep1ViewModel
import com.divercity.android.features.groups.createeditgroup.step3.CreateEditGroupStep3ViewModel
import com.divercity.android.features.groups.createnewpost.CreateNewPostViewModel
import com.divercity.android.features.groups.createtopic.CreateTopicViewModel
import com.divercity.android.features.groups.deletegroupadmin.DeleteGroupAdminViewModel
import com.divercity.android.features.groups.deletegroupmember.DeleteGroupMemberViewModel
import com.divercity.android.features.groups.followedgroups.FollowedGroupsViewModel
import com.divercity.android.features.groups.groupanswers.AnswerViewModel
import com.divercity.android.features.groups.groupdetail.GroupDetailViewModel
import com.divercity.android.features.groups.groupdetail.about.TabAboutGroupDetailViewModel
import com.divercity.android.features.groups.groupdetail.conversation.GroupConversationViewModel
import com.divercity.android.features.groups.mygroups.MyGroupsViewModel
import com.divercity.android.features.groups.onboarding.SelectGroupViewModel
import com.divercity.android.features.groups.selectfollowedgroup.SelectFollowedGroupViewModel
import com.divercity.android.features.groups.trending.TrendingGroupsViewModel
import com.divercity.android.features.groups.viewmodel.GroupViewModel
import com.divercity.android.features.groups.yourgroups.YourGroupsViewModel
import com.divercity.android.features.home.HomeActivityViewModel
import com.divercity.android.features.home.home.HomeRecommendedConnectionsViewModel
import com.divercity.android.features.home.home.HomeRecommendedViewModel
import com.divercity.android.features.home.home.HomeViewModel
import com.divercity.android.features.home.people.TabPeopleViewModel
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingViewModel
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryViewModel
import com.divercity.android.features.invitations.contacts.InvitePhoneContactsViewModel
import com.divercity.android.features.invitations.users.InviteUsersViewModel
import com.divercity.android.features.jobs.TabJobsViewModel
import com.divercity.android.features.jobs.applicants.JobApplicantsViewModel
import com.divercity.android.features.jobs.applications.JobsApplicationsViewModel
import com.divercity.android.features.jobs.detail.detail.JobDetailViewModel
import com.divercity.android.features.jobs.detail.poster.JobDescriptionPosterViewModel
import com.divercity.android.features.jobs.jobposting.JobPostingViewModel
import com.divercity.android.features.jobs.jobposting.jobtype.JobTypeViewModel
import com.divercity.android.features.jobs.jobposting.sharetogroup.ShareJobGroupViewModel
import com.divercity.android.features.jobs.jobs.JobsListViewModel
import com.divercity.android.features.jobs.mypostings.MyJobsPostingsViewModel
import com.divercity.android.features.jobs.savedjobs.SavedJobsViewModel
import com.divercity.android.features.jobs.similarjobs.SimilarJobListViewModel
import com.divercity.android.features.linkedin.LinkedinViewModel
import com.divercity.android.features.location.base.SelectLocationViewModel
import com.divercity.android.features.location.onboarding.OnboardingLocationViewModel
import com.divercity.android.features.login.step1.EnterEmailViewModel
import com.divercity.android.features.login.step2.LoginViewModel
import com.divercity.android.features.major.base.SelectMajorViewModel
import com.divercity.android.features.major.onboarding.OnboardingMajorViewModel
import com.divercity.android.features.multipleuseraction.MultipleUserActionViewModel
import com.divercity.android.features.onboarding.profileprompt.ProfilePromptViewModel
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsViewModel
import com.divercity.android.features.onboarding.selectoccupation.SelectOccupationViewModel
import com.divercity.android.features.onboarding.selectoccupationofinterests.SelectOOIViewModel
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeViewModel
import com.divercity.android.features.onboarding.uploadresume.UploadResumeViewModel
import com.divercity.android.features.password.changepassword.ChangePasswordViewModel
import com.divercity.android.features.password.resetpassword.ResetPasswordViewModel
import com.divercity.android.features.school.base.SelectSchoolViewModel
import com.divercity.android.features.school.onboarding.OnboardingSchoolViewModel
import com.divercity.android.features.search.SearchViewModel
import com.divercity.android.features.settings.ProfileSettingsViewModel
import com.divercity.android.features.settings.accountsettings.AccountSettingsViewModel
import com.divercity.android.features.signup.SignUpViewModel
import com.divercity.android.features.singleuseraction.SingleUserActionViewModel
import com.divercity.android.features.skill.base.SelectSkillViewModel
import com.divercity.android.features.skill.editskills.EditUserSkillViewModel
import com.divercity.android.features.skill.jobskills.JobSkillsViewModel
import com.divercity.android.features.skill.onboarding.OnboardingSkillViewModel
import com.divercity.android.features.splash.SplashViewModel
import com.divercity.android.features.user.addediteducation.AddEditEducationViewModel
import com.divercity.android.features.user.addeditworkexperience.AddEditWorkExperienceViewModel
import com.divercity.android.features.user.allconnections.AllConnectionsViewModel
import com.divercity.android.features.user.editexperienceeducation.EditExperienceEducationViewModel
import com.divercity.android.features.user.editpersonal.PersonalSettingsViewModel
import com.divercity.android.features.user.myinterests.InterestsViewModel
import com.divercity.android.features.user.profilecurrentuser.CurrentUserProfileViewModel
import com.divercity.android.features.user.profilecurrentuser.tabprofile.TabProfileViewModel
import com.divercity.android.features.user.profileotheruser.OtherUserProfileViewModel
import com.divercity.android.features.user.profileotheruser.tabprofile.TabOtherUserProfileViewModel
import com.divercity.android.features.user.userconnections.ConnectionsViewModel
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
    @ViewModelKey(HomeRecommendedViewModel::class)
    abstract fun bindsHomeRecommendedViewModel(viewModel: HomeRecommendedViewModel): ViewModel

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
    @ViewModelKey(OnboardingSchoolViewModel::class)
    abstract fun bindsOnboardingSchoolViewModel(viewModel: OnboardingSchoolViewModel): ViewModel

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
    @ViewModelKey(OnboardingMajorViewModel::class)
    abstract fun bindsOnboardingMajorViewModel(viewModel: OnboardingMajorViewModel): ViewModel

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
    @ViewModelKey(CurrentUserProfileViewModel::class)
    abstract fun bindsProfileViewModel(viewModel: CurrentUserProfileViewModel): ViewModel

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
    @ViewModelKey(SelectFollowedGroupViewModel::class)
    abstract fun bindsFollowingGroupsViewModel(viewModel: SelectFollowedGroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatsViewModel::class)
    abstract fun bindsDirectMessagesViewModel(viewModel: com.divercity.android.features.chat.recentchats.oldrecentchats.ChatsViewModel): ViewModel

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
    @ViewModelKey(CreateEditGroupStep3ViewModel::class)
    abstract fun bindsGroupDescriptionViewModel(viewModel: CreateEditGroupStep3ViewModel): ViewModel

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
    @ViewModelKey(NewGroupChatViewModel::class)
    abstract fun bindsNewGroupChatViewModel(viewModel: NewGroupChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateGroupChatViewModel::class)
    abstract fun bindsCreateGroupChatViewModel(viewModel: CreateGroupChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountSettingsViewModel::class)
    abstract fun bindsAccountSettingsViewModel(viewModel: AccountSettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InvitePhoneContactsViewModel::class)
    abstract fun bindsPhoneContactsViewModel(viewModel: InvitePhoneContactsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TabActivityViewModel::class)
    abstract fun bindsActivityViewModel(viewModel: TabActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel::class)
    abstract fun bindsNotificationsViewModel(viewModel: NotificationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConnectionRequestsViewModel::class)
    abstract fun bindsConnectionRequestsViewModel(viewModel: ConnectionRequestsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateTopicViewModel::class)
    abstract fun bindsCreateTopicViewModel(viewModel: CreateTopicViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnswerViewModel::class)
    abstract fun bindsAnswerViewModel(viewModel: AnswerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectSkillViewModel::class)
    abstract fun bindsSelectSkillViewModel(viewModel: SelectSkillViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UploadResumeViewModel::class)
    abstract fun bindsUploadResumeViewModel(viewModel: UploadResumeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingSkillViewModel::class)
    abstract fun bindsOnboardingSkillViewModel(viewModel: OnboardingSkillViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditUserSkillViewModel::class)
    abstract fun bindsToolbarSkillViewModel(viewModel: EditUserSkillViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OtherUserProfileViewModel::class)
    abstract fun bindsOtherUserProfileViewModel(viewModel: OtherUserProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TabOtherUserProfileViewModel::class)
    abstract fun bindsTabOtherUserProfileViewModel(viewModel: TabOtherUserProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompaniesViewModel::class)
    abstract fun bindsCompaniesViewModel(viewModel: CompaniesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateEditGroupStep1ViewModel::class)
    abstract fun bindsCreateEditGroupStep1ViewModel(viewModel: CreateEditGroupStep1ViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllConnectionsViewModel::class)
    abstract fun bindsAllConnectionsViewModel(viewModel: AllConnectionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InviteUsersViewModel::class)
    abstract fun bindsInviteUsersViewModel(viewModel: InviteUsersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TabPeopleViewModel::class)
    abstract fun bindsTabPeopleViewModel(viewModel: TabPeopleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyCompaniesViewModel::class)
    abstract fun bindsMyCompaniesViewModel(viewModel: MyCompaniesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompanyDetailViewModel::class)
    abstract fun bindsCompanyDetailViewModel(viewModel: CompanyDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmployeesViewModel::class)
    abstract fun bindsEmployeesViewModel(viewModel: EmployeesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobPostingsByCompanyViewModel::class)
    abstract fun bindsJobPostingsByCompanyViewModel(viewModel: JobPostingsByCompanyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompanyAdminViewModel::class)
    abstract fun bindsCompanyAdminViewModel(viewModel: CompanyAdminViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SingleUserActionViewModel::class)
    abstract fun bindsCompanyAdminAddViewModel(viewModel: SingleUserActionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditWorkExperienceViewModel::class)
    abstract fun bindsAddWorkExperienceViewModel(viewModel: AddEditWorkExperienceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompanyAddAdminViewModel::class)
    abstract fun bindsCompanyAddAdminViewModel(viewModel: CompanyAddAdminViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupViewModel::class)
    abstract fun bindsGroupViewModel(viewModel: GroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MultipleUserActionViewModel::class)
    abstract fun bindsMultipleUserActionViewModel(viewModel: MultipleUserActionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeleteGroupAdminViewModel::class)
    abstract fun bindsDeleteGroupAdminViewModel(viewModel: DeleteGroupAdminViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeleteCompanyAdminViewModel::class)
    abstract fun bindsDeleteCompanyAdminViewModel(viewModel: DeleteCompanyAdminViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiversityRatingViewModel::class)
    abstract fun bindsDiversityRatingViewModel(viewModel: DiversityRatingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RateCompanyViewModel::class)
    abstract fun bindsRateCompanyViewModel(viewModel: RateCompanyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RateCompanyDiversityDialogViewModel::class)
    abstract fun bindsRateCompanyDiversityDialogViewModel(viewModel: RateCompanyDiversityDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun bindsResetPasswordViewModel(viewModel: ResetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun bindsChangePasswordViewModel(viewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindsSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateNewPostViewModel::class)
    abstract fun bindsCreateNewPostViewModel(viewModel: CreateNewPostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompaniesMyCompaniesViewModel::class)
    abstract fun bindsCompaniesMyCompaniesViewModel(viewModel: CompaniesMyCompaniesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeleteGroupMemberViewModel::class)
    abstract fun bindsDeleteGroupMemberViewModel(viewModel: DeleteGroupMemberViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(YourGroupsViewModel::class)
    abstract fun bindsYourGroupsViewModel(viewModel: YourGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowedGroupsViewModel::class)
    abstract fun bindsFollowedGroupsViewModel(viewModel: FollowedGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeRecommendedConnectionsViewModel::class)
    abstract fun bindsHomeRecommendedConnectionsViewModel(viewModel: HomeRecommendedConnectionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobSearchFilterDialogViewModel::class)
    abstract fun bindsJobSearchFilterViewModel(viewModel: JobSearchFilterDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditExperienceEducationViewModel::class)
    abstract fun bindsEditExperienceEducationViewModel(viewModel: EditExperienceEducationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditEducationViewModel::class)
    abstract fun bindsAddEducationViewModel(viewModel: AddEditEducationViewModel): ViewModel
}