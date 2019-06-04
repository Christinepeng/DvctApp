package com.divercity.android.di.module

import com.divercity.android.features.activity.TabActivityFragment
import com.divercity.android.features.activity.connectionrequests.ConnectionRequestsFragment
import com.divercity.android.features.activity.notifications.NotificationsFragment
import com.divercity.android.features.agerange.base.SelectAgeFragment
import com.divercity.android.features.agerange.onboarding.OnboardingAgeFragment
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeFragment
import com.divercity.android.features.chat.chat.ChatFragment
import com.divercity.android.features.chat.creategroupchat.CreateGroupChatFragment
import com.divercity.android.features.chat.newchat.NewChatFragment
import com.divercity.android.features.chat.newgroupchat.NewGroupChatFragment
import com.divercity.android.features.chat.recentchats.oldrecentchats.ChatsFragment
import com.divercity.android.features.company.companies.CompaniesFragment
import com.divercity.android.features.company.companiesmycompanies.CompaniesMyCompaniesFragment
import com.divercity.android.features.company.companyaddadmin.CompanyAddAdminFragment
import com.divercity.android.features.company.companyadmin.CompanyAdminFragment
import com.divercity.android.features.company.companydetail.CompanyDetailFragment
import com.divercity.android.features.company.companydetail.about.CompanyDetailAboutFragment
import com.divercity.android.features.company.companydetail.employees.EmployeesFragment
import com.divercity.android.features.company.companydetail.jobpostings.JobPostingsByCompanyFragment
import com.divercity.android.features.company.companysize.CompanySizesFragment
import com.divercity.android.features.company.createcompany.CreateCompanyFragment
import com.divercity.android.features.company.deleteadmincompany.DeleteCompanyAdminFragment
import com.divercity.android.features.company.diversityrating.DiversityRatingFragment
import com.divercity.android.features.company.mycompanies.MyCompaniesFragment
import com.divercity.android.features.company.ratecompany.RateCompanyFragment
import com.divercity.android.features.company.selectcompany.base.SelectCompanyFragment
import com.divercity.android.features.company.selectcompany.onboarding.OnboardingCompanyFragment
import com.divercity.android.features.company.selectcompany.withtoolbar.ToolbarCompanyFragment
import com.divercity.android.features.dialogs.jobapplication.JobApplicationDialogFragment
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.dialogs.jobapplysuccess.JobApplySuccessDialogFragment
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogFragment
import com.divercity.android.features.dialogs.ratecompany.RateCompanyDiversityDialogFragment
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.android.features.ethnicity.base.SelectEthnicityFragment
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityFragment
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityFragment
import com.divercity.android.features.gender.base.SelectGenderFragment
import com.divercity.android.features.gender.onboarding.OnboardingGenderFragment
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderFragment
import com.divercity.android.features.groups.TabGroupsFragment
import com.divercity.android.features.groups.allgroups.AllGroupsFragment
import com.divercity.android.features.groups.createeditgroup.step1.CreateEditGroupStep1Fragment
import com.divercity.android.features.groups.createeditgroup.step3.CreateEditGroupStep3Fragment
import com.divercity.android.features.groups.createnewpost.CreateNewPostFragment
import com.divercity.android.features.groups.createtopic.CreateTopicFragment
import com.divercity.android.features.groups.deletegroupadmin.DeleteGroupAdminFragment
import com.divercity.android.features.groups.deletegroupmember.DeleteGroupMemberFragment
import com.divercity.android.features.groups.followedgroups.FollowedGroupsFragment
import com.divercity.android.features.groups.groupanswers.AnswerFragment
import com.divercity.android.features.groups.groupdetail.GroupDetailFragment
import com.divercity.android.features.groups.groupdetail.about.TabAboutGroupDetailFragment
import com.divercity.android.features.groups.groupdetail.conversation.GroupConversationFragment
import com.divercity.android.features.groups.mygroups.MyGroupsFragment
import com.divercity.android.features.groups.onboarding.SelectGroupFragment
import com.divercity.android.features.groups.selectfollowedgroup.SelectFollowedGroupFragment
import com.divercity.android.features.groups.trending.TrendingGroupsFragment
import com.divercity.android.features.groups.yourgroups.YourGroupsFragment
import com.divercity.android.features.home.home.HomeFragment
import com.divercity.android.features.home.people.TabPeopleFragment
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingFragment
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryFragment
import com.divercity.android.features.invitations.contacts.InvitePhoneContactsFragment
import com.divercity.android.features.invitations.users.InviteUsersFragment
import com.divercity.android.features.jobs.TabJobsFragment
import com.divercity.android.features.jobs.applicants.JobApplicantsFragment
import com.divercity.android.features.jobs.applications.JobsApplicationsFragment
import com.divercity.android.features.jobs.detail.aboutcompany.TabAboutCompanyFragment
import com.divercity.android.features.jobs.detail.detail.JobDetailFragment
import com.divercity.android.features.jobs.detail.jobdescription.TabJobDescriptionFragment
import com.divercity.android.features.jobs.detail.poster.JobDescriptionPosterFragment
import com.divercity.android.features.jobs.detail.poster.similarjobs.SimilarJobsFragment
import com.divercity.android.features.jobs.jobposting.JobPostingFragment
import com.divercity.android.features.jobs.jobposting.jobtype.JobTypeFragment
import com.divercity.android.features.jobs.jobposting.sharetogroup.ShareJobGroupFragment
import com.divercity.android.features.jobs.jobs.JobsListFragment
import com.divercity.android.features.jobs.jobs.search.searchfilter.JobSearchFilterFragment
import com.divercity.android.features.jobs.jobs.search.searchfiltercompany.JobSearchFilterCompanyFragment
import com.divercity.android.features.jobs.jobs.search.searchfiltercompanyindustry.JobSearchFilterCompanyIndustryFragment
import com.divercity.android.features.jobs.jobs.search.searchfiltercompanysize.JobSearchFilterCompanySizeFragment
import com.divercity.android.features.jobs.jobs.search.searchfilterlocation.JobSearchFilterLocationFragment
import com.divercity.android.features.jobs.jobs.search.searchfilterview.JobSearchFilterViewFragment
import com.divercity.android.features.jobs.mypostings.MyJobsPostingsFragment
import com.divercity.android.features.jobs.savedjobs.SavedJobsFragment
import com.divercity.android.features.jobs.similarjobs.SimilarJobListFragment
import com.divercity.android.features.linkedin.LinkedinFragment
import com.divercity.android.features.loadurl.LoadUrlFragment
import com.divercity.android.features.location.base.SelectLocationFragment
import com.divercity.android.features.location.onboarding.OnboardingLocationFragment
import com.divercity.android.features.location.withtoolbar.ToolbarLocationFragment
import com.divercity.android.features.login.step1.EnterEmailFragment
import com.divercity.android.features.login.step2.LoginFragment
import com.divercity.android.features.multipleuseraction.MultipleUserActionFragment
import com.divercity.android.features.onboarding.profileprompt.ProfilePromptFragment
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsFragment
import com.divercity.android.features.onboarding.selectmajor.SelectMajorFragment
import com.divercity.android.features.onboarding.selectoccupation.SelectOccupationFragment
import com.divercity.android.features.onboarding.selectoccupationofinterests.SelectOOIFragment
import com.divercity.android.features.onboarding.selectschool.SelectSchoolFragment
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeFragment
import com.divercity.android.features.onboarding.uploadresume.UploadResumeFragment
import com.divercity.android.features.password.changepassword.ChangePasswordFragment
import com.divercity.android.features.password.resetpassword.ResetPasswordFragment
import com.divercity.android.features.search.SearchFragment
import com.divercity.android.features.settings.ProfileSettingsFragment
import com.divercity.android.features.settings.accountsettings.AccountSettingsFragment
import com.divercity.android.features.signup.SignUpFragment
import com.divercity.android.features.singleuseraction.SingleUserActionFragment
import com.divercity.android.features.skill.base.SelectSkillFragment
import com.divercity.android.features.skill.editskills.EditUserSkillFragment
import com.divercity.android.features.skill.jobskills.JobSkillsFragment
import com.divercity.android.features.skill.onboarding.OnboardingSkillFragment
import com.divercity.android.features.splash.SplashFragment
import com.divercity.android.features.user.allconnections.AllConnectionsFragment
import com.divercity.android.features.user.editpersonal.PersonalSettingsFragment
import com.divercity.android.features.user.experience.AddWorkExperienceFragment
import com.divercity.android.features.user.myinterests.InterestsFragment
import com.divercity.android.features.user.profilecurrentuser.CurrentUserProfileFragment
import com.divercity.android.features.user.profilecurrentuser.tabprofile.TabProfileFragment
import com.divercity.android.features.user.profileotheruser.OtherUserProfileFragment
import com.divercity.android.features.user.profileotheruser.tabprofile.TabOtherUserProfileFragment
import com.divercity.android.features.user.userconnections.ConnectionsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by lucas on 25/10/2018.
 */

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun bindSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun bindSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun bindEnterEmailFragment(): EnterEmailFragment

    @ContributesAndroidInjector
    abstract fun bindGroupsFragment(): TabGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun bindTabActivityFragment(): TabActivityFragment

    @ContributesAndroidInjector
    abstract fun bindTabPeopleFragment(): TabPeopleFragment

    @ContributesAndroidInjector
    abstract fun bindJobsFragment(): TabJobsFragment

    @ContributesAndroidInjector
    abstract fun bindProfileFragment(): CurrentUserProfileFragment

    @ContributesAndroidInjector
    abstract fun bindProfilePromptFragment(): ProfilePromptFragment

    @ContributesAndroidInjector
    abstract fun bindSelectUserTypeFragment(): SelectUserTypeFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSchoolFragment(): SelectSchoolFragment

    @ContributesAndroidInjector
    abstract fun bindSelectBirthdayFragment(): SelectAgeFragment

    @ContributesAndroidInjector
    abstract fun bindSelectCompanyFragment(): SelectCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingLocationFragment(): OnboardingLocationFragment

    @ContributesAndroidInjector
    abstract fun bindSelectEthnicityFragment(): SelectEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindSelectGenderFragment(): SelectGenderFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSingleIndustryFragment(): SelectSingleIndustryFragment

    @ContributesAndroidInjector
    abstract fun bindSelectMajorFragment(): SelectMajorFragment

    @ContributesAndroidInjector
    abstract fun bindSelectGroupFragment(): SelectGroupFragment

    @ContributesAndroidInjector
    abstract fun bindJobsListFragment(): JobsListFragment

    @ContributesAndroidInjector
    abstract fun bindMyPostingsFragment(): MyJobsPostingsFragment

    @ContributesAndroidInjector
    abstract fun bindLinkedinFragment(): LinkedinFragment

    @ContributesAndroidInjector
    abstract fun bindJobsSavedFragment(): SavedJobsFragment

    @ContributesAndroidInjector
    abstract fun bindJobPostingFragment(): JobPostingFragment

    @ContributesAndroidInjector
    abstract fun bindSelectLocationFragment(): SelectLocationFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarLocationFragment(): ToolbarLocationFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarCompanyFragment(): ToolbarCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingCompanyFragment(): OnboardingCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindJobTypeFragment(): JobTypeFragment

    @ContributesAndroidInjector
    abstract fun bindShareJobGroupFragment(): ShareJobGroupFragment

    @ContributesAndroidInjector
    abstract fun bindJobSkillsFragment(): JobSkillsFragment

    @ContributesAndroidInjector
    abstract fun bindAllGroupsFragment(): AllGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindTrendingGroupsFragment(): TrendingGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindMyGroupsFragment(): MyGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindJobDescriptionSeekerFragment(): JobDetailFragment

    @ContributesAndroidInjector
    abstract fun bindJobDescriptionPosterFragment(): JobDescriptionPosterFragment

    @ContributesAndroidInjector
    abstract fun bindTabJobDescriptionFragment(): TabJobDescriptionFragment

    @ContributesAndroidInjector
    abstract fun bindTabAboutCompanyFragment(): TabAboutCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindSimilarJobListFragment(): SimilarJobListFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplyDialogFragment(): JobApplyDialogFragment

    @ContributesAndroidInjector
    abstract fun bindJobsApplicationsFragment(): JobsApplicationsFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplicantsFragment(): JobApplicantsFragment

    @ContributesAndroidInjector
    abstract fun bindRecentDocsDialogFragment(): RecentDocsDialogFragment

    @ContributesAndroidInjector
    abstract fun bindCreateCompanyFragment(): CreateCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingIndustryFragment(): SelectIndustryOnboardingFragment

    @ContributesAndroidInjector
    abstract fun bindCompanySizesFragment(): CompanySizesFragment

    @ContributesAndroidInjector
    abstract fun bindSimilarJobsFragment(): SimilarJobsFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplySuccessDialogFragment(): JobApplySuccessDialogFragment

    @ContributesAndroidInjector
    abstract fun bindSelectOccupationFragment(): SelectOccupationFragment

    @ContributesAndroidInjector
    abstract fun bindSelectOOIFragment(): SelectOOIFragment

    @ContributesAndroidInjector
    abstract fun bindSelectInterestsFragment(): SelectInterestsFragment

    @ContributesAndroidInjector
    abstract fun bindTabProfileFragment(): TabProfileFragment

    @ContributesAndroidInjector
    abstract fun bindFollowerFragment(): ConnectionsFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarEthnicityFragment(): ToolbarEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarGenderFragment(): ToolbarGenderFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingEthnicityFragment(): OnboardingEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingGenderFragment(): OnboardingGenderFragment

    @ContributesAndroidInjector
    abstract fun bindFollowingGroupsFragment(): SelectFollowedGroupFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingAgeFragment(): OnboardingAgeFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarAgeFragment(): ToolbarAgeFragment

    @ContributesAndroidInjector
    abstract fun bindNewChatFragment(): NewChatFragment

    @ContributesAndroidInjector
    abstract fun bindGroupDetailFragment(): GroupDetailFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplicationDialogFragment(): JobApplicationDialogFragment

    @ContributesAndroidInjector
    abstract fun bindCreateGroupFragment(): CreateEditGroupStep1Fragment

    @ContributesAndroidInjector
    abstract fun bindChatFragment(): ChatFragment

    @ContributesAndroidInjector
    abstract fun bindTabAboutGroupDetailFragment(): TabAboutGroupDetailFragment

    @ContributesAndroidInjector
    abstract fun bindGroupConversationFragment(): GroupConversationFragment

    @ContributesAndroidInjector
    abstract fun bindGroupDescriptionFragment(): CreateEditGroupStep3Fragment

    @ContributesAndroidInjector
    abstract fun bindProfileSettingsFragment(): ProfileSettingsFragment

    @ContributesAndroidInjector
    abstract fun bindPersonalSettingsFragment(): PersonalSettingsFragment

    @ContributesAndroidInjector
    abstract fun bindInterestsFragment(): InterestsFragment

    @ContributesAndroidInjector
    abstract fun bindChatsFragment(): ChatsFragment

    @ContributesAndroidInjector
    abstract fun bindNewGroupChatFragment(): NewGroupChatFragment

    @ContributesAndroidInjector
    abstract fun bindCreateGroupChatFragment(): CreateGroupChatFragment

    @ContributesAndroidInjector
    abstract fun bindAccountSettingsFragment(): AccountSettingsFragment

    @ContributesAndroidInjector
    abstract fun bindPhoneContactsFragment(): InvitePhoneContactsFragment

    @ContributesAndroidInjector
    abstract fun bindConnectionRequestsFragment(): ConnectionRequestsFragment

    @ContributesAndroidInjector
    abstract fun bindNotificationsFragment(): NotificationsFragment

    @ContributesAndroidInjector
    abstract fun bindCreateTopicFragment(): CreateTopicFragment

    @ContributesAndroidInjector
    abstract fun bindAnswerFragment(): AnswerFragment

    @ContributesAndroidInjector
    abstract fun bindLoadUrlFragment(): LoadUrlFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSkillFragment(): SelectSkillFragment

    @ContributesAndroidInjector
    abstract fun bindUploadResumeFragment(): UploadResumeFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingSkillFragment(): OnboardingSkillFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarSkillFragment(): EditUserSkillFragment

    @ContributesAndroidInjector
    abstract fun bindOtherUserProfileFragment(): OtherUserProfileFragment

    @ContributesAndroidInjector
    abstract fun bindTabOtherUserProfileFragment(): TabOtherUserProfileFragment

    @ContributesAndroidInjector
    abstract fun bindCompaniesFragment(): CompaniesFragment

    @ContributesAndroidInjector
    abstract fun bindAllConnectionsFragment(): AllConnectionsFragment

    @ContributesAndroidInjector
    abstract fun bindInviteUsersFragment(): InviteUsersFragment

    @ContributesAndroidInjector
    abstract fun bindMyCompaniesFragment(): MyCompaniesFragment

    @ContributesAndroidInjector
    abstract fun bindCompanyDetailFragment(): CompanyDetailFragment

    @ContributesAndroidInjector
    abstract fun bindEmployeesFragment(): EmployeesFragment

    @ContributesAndroidInjector
    abstract fun bindCompanyDetailAboutFragment(): CompanyDetailAboutFragment

    @ContributesAndroidInjector
    abstract fun bindJobPostingsByCompanyFragment(): JobPostingsByCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindCompanyAdminFragment(): CompanyAdminFragment

    @ContributesAndroidInjector
    abstract fun bindCompanyAdminAddFragment(): SingleUserActionFragment

    @ContributesAndroidInjector
    abstract fun bindAddWorkExperienceFragment(): AddWorkExperienceFragment

    @ContributesAndroidInjector
    abstract fun bindCompanyAddAdminFragment(): CompanyAddAdminFragment

    @ContributesAndroidInjector
    abstract fun bindMultipleUserActionFragment(): MultipleUserActionFragment

    @ContributesAndroidInjector
    abstract fun bindDeleteGroupAdminFragment(): DeleteGroupAdminFragment

    @ContributesAndroidInjector
    abstract fun bindDeleteCompanyAdminFragment(): DeleteCompanyAdminFragment

    @ContributesAndroidInjector
    abstract fun bindDiversityRatingFragment(): DiversityRatingFragment

    @ContributesAndroidInjector
    abstract fun bindRateCompanyFragment(): RateCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindRateCompanyDiversityDialogFragment(): RateCompanyDiversityDialogFragment

    @ContributesAndroidInjector
    abstract fun bindResetPasswordFragment(): ResetPasswordFragment

    @ContributesAndroidInjector
    abstract fun bindChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector
    abstract fun bindSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun bindCreateNewPostFragment(): CreateNewPostFragment

    @ContributesAndroidInjector
    abstract fun bindCompaniesMyCompaniesFragment(): CompaniesMyCompaniesFragment

    @ContributesAndroidInjector
    abstract fun bindDeleteGroupMemberFragment(): DeleteGroupMemberFragment

    @ContributesAndroidInjector
    abstract fun bindYourGroupsFragment(): YourGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindFollowedGroupsFragment(): FollowedGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindJobSearchFilterFragment(): JobSearchFilterFragment

    @ContributesAndroidInjector
    abstract fun bindJobSearchFilterDialogFragment(): JobSearchFilterDialogFragment

    @ContributesAndroidInjector
    abstract fun bindJobSearchFilterViewFragment(): JobSearchFilterViewFragment

    @ContributesAndroidInjector
    abstract fun bindJobSearchFilterLocationFragment(): JobSearchFilterLocationFragment

    @ContributesAndroidInjector
    abstract fun bindJobSearchFilterCompanyFragment(): JobSearchFilterCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindJobSearchFilterCompanySizeFragment(): JobSearchFilterCompanySizeFragment

    @ContributesAndroidInjector
    abstract fun bindJobSearchFilterCompanyIndustryFragment(): JobSearchFilterCompanyIndustryFragment
}