package com.divercity.android.di.module

import com.divercity.android.features.activity.ActivityFragment
import com.divercity.android.features.activity.connectionrequests.ConnectionRequestsFragment
import com.divercity.android.features.activity.module.NotificationsModule
import com.divercity.android.features.activity.notifications.NotificationsFragment
import com.divercity.android.features.agerange.base.SelectAgeFragment
import com.divercity.android.features.agerange.onboarding.OnboardingAgeFragment
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeFragment
import com.divercity.android.features.chat.chat.ChatFragment
import com.divercity.android.features.chat.creategroupchat.CreateGroupChatFragment
import com.divercity.android.features.chat.newchat.NewChatFragment
import com.divercity.android.features.chat.newgroupchat.NewGroupChatFragment
import com.divercity.android.features.chat.recentchats.oldrecentchats.ChatsFragment
import com.divercity.android.features.company.base.SelectCompanyFragment
import com.divercity.android.features.company.companysize.CompanySizesFragment
import com.divercity.android.features.company.createcompany.CreateCompanyFragment
import com.divercity.android.features.company.onboarding.OnboardingCompanyFragment
import com.divercity.android.features.company.withtoolbar.ToolbarCompanyFragment
import com.divercity.android.features.contacts.InvitePhoneContactsFragment
import com.divercity.android.features.dialogs.jobapplication.JobApplicationDialogFragment
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.dialogs.jobapplysuccess.JobApplySuccessDialogFragment
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.android.features.ethnicity.base.SelectEthnicityFragment
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityFragment
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityFragment
import com.divercity.android.features.gender.base.SelectGenderFragment
import com.divercity.android.features.gender.onboarding.OnboardingGenderFragment
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderFragment
import com.divercity.android.features.groups.TabGroupsFragment
import com.divercity.android.features.groups.all.AllGroupsFragment
import com.divercity.android.features.groups.creategroup.step1.CreateGroupFragment
import com.divercity.android.features.groups.creategroup.step3.GroupDescriptionFragment
import com.divercity.android.features.groups.groupdetail.GroupDetailFragment
import com.divercity.android.features.groups.groupdetail.about.TabAboutGroupDetailFragment
import com.divercity.android.features.groups.groupdetail.conversation.GroupConversationFragment
import com.divercity.android.features.groups.groupdetail.module.GroupDetailModule
import com.divercity.android.features.groups.module.GroupsModule
import com.divercity.android.features.groups.mygroups.MyGroupsFragment
import com.divercity.android.features.groups.onboarding.SelectGroupFragment
import com.divercity.android.features.groups.trending.TrendingGroupsFragment
import com.divercity.android.features.home.home.HomeFragment
import com.divercity.android.features.home.settings.SettingsFragment
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingFragment
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryFragment
import com.divercity.android.features.jobposting.JobPostingFragment
import com.divercity.android.features.jobposting.jobtype.JobTypeFragment
import com.divercity.android.features.jobposting.sharetogroup.ShareJobGroupFragment
import com.divercity.android.features.jobposting.skills.JobSkillsFragment
import com.divercity.android.features.jobs.TabJobsFragment
import com.divercity.android.features.jobs.applicants.JobApplicantsFragment
import com.divercity.android.features.jobs.applications.JobsApplicationsFragment
import com.divercity.android.features.jobs.description.aboutcompany.TabAboutCompanyFragment
import com.divercity.android.features.jobs.description.detail.JobDetailFragment
import com.divercity.android.features.jobs.description.detail.module.JobDetailModule
import com.divercity.android.features.jobs.description.jobdescription.TabJobDescriptionFragment
import com.divercity.android.features.jobs.description.poster.JobDescriptionPosterFragment
import com.divercity.android.features.jobs.description.poster.module.JobDescriptionPosterModule
import com.divercity.android.features.jobs.description.poster.similarjobs.SimilarJobsFragment
import com.divercity.android.features.jobs.jobs.JobsListFragment
import com.divercity.android.features.jobs.module.JobsModule
import com.divercity.android.features.jobs.mypostings.MyJobsPostingsFragment
import com.divercity.android.features.jobs.savedjobs.SavedJobsFragment
import com.divercity.android.features.jobs.similarjobs.SimilarJobListFragment
import com.divercity.android.features.linkedin.LinkedinFragment
import com.divercity.android.features.location.base.SelectLocationFragment
import com.divercity.android.features.location.onboarding.OnboardingLocationFragment
import com.divercity.android.features.location.withtoolbar.ToolbarLocationFragment
import com.divercity.android.features.login.step1.EnterEmailFragment
import com.divercity.android.features.login.step2.LoginFragment
import com.divercity.android.features.onboarding.profileprompt.ProfilePromptFragment
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsFragment
import com.divercity.android.features.onboarding.selectmajor.SelectMajorFragment
import com.divercity.android.features.onboarding.selectoccupation.SelectOccupationFragment
import com.divercity.android.features.onboarding.selectoccupationofinterests.SelectOOIFragment
import com.divercity.android.features.onboarding.selectschool.SelectSchoolFragment
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeFragment
import com.divercity.android.features.profile.ProfileFragment
import com.divercity.android.features.profile.module.ProfileModule
import com.divercity.android.features.profile.settings.ProfileSettingsFragment
import com.divercity.android.features.profile.settings.accountsettings.AccountSettingsFragment
import com.divercity.android.features.profile.settings.interests.InterestsFragment
import com.divercity.android.features.profile.settings.personalsettings.PersonalSettingsFragment
import com.divercity.android.features.profile.tabconnections.ConnectionsFragment
import com.divercity.android.features.profile.tabfollowing.FollowingFragment
import com.divercity.android.features.profile.tabgroups.FollowingGroupsFragment
import com.divercity.android.features.profile.tabprofile.TabProfileFragment
import com.divercity.android.features.signup.SignUpFragment
import com.divercity.android.features.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by lucas on 25/10/2018.
 */

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun bindSplashFragment (): SplashFragment

    @ContributesAndroidInjector
    abstract fun bindLoginFragment (): LoginFragment

    @ContributesAndroidInjector
    abstract fun bindSignUpFragment (): SignUpFragment

    @ContributesAndroidInjector
    abstract fun bindEnterEmailFragment (): EnterEmailFragment

    @ContributesAndroidInjector(modules = [GroupsModule::class])
    abstract fun bindGroupsFragment (): TabGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindHomeFragment (): HomeFragment

    @ContributesAndroidInjector(modules = [JobsModule::class])
    abstract fun bindJobsFragment (): TabJobsFragment

    @ContributesAndroidInjector(modules = [NotificationsModule::class])
    abstract fun bindActivityFragment (): ActivityFragment

    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun bindProfileFragment (): ProfileFragment

    @ContributesAndroidInjector
    abstract fun bindSettingsFragment (): SettingsFragment

    @ContributesAndroidInjector
    abstract fun bindProfilePromptFragment (): ProfilePromptFragment

    @ContributesAndroidInjector
    abstract fun bindSelectUserTypeFragment (): SelectUserTypeFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSchoolFragment (): SelectSchoolFragment

    @ContributesAndroidInjector
    abstract fun bindSelectBirthdayFragment (): SelectAgeFragment

    @ContributesAndroidInjector
    abstract fun bindSelectCompanyFragment (): SelectCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingLocationFragment (): OnboardingLocationFragment

    @ContributesAndroidInjector
    abstract fun bindSelectEthnicityFragment (): SelectEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindSelectGenderFragment (): SelectGenderFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSingleIndustryFragment (): SelectSingleIndustryFragment

    @ContributesAndroidInjector
    abstract fun bindSelectMajorFragment (): SelectMajorFragment

    @ContributesAndroidInjector
    abstract fun bindSelectGroupFragment (): SelectGroupFragment

    @ContributesAndroidInjector
    abstract fun bindJobsListFragment (): JobsListFragment

    @ContributesAndroidInjector
    abstract fun bindMyPostingsFragment (): MyJobsPostingsFragment

    @ContributesAndroidInjector
    abstract fun bindLinkedinFragment (): LinkedinFragment

    @ContributesAndroidInjector
    abstract fun bindJobsSavedFragment (): SavedJobsFragment

    @ContributesAndroidInjector
    abstract fun bindJobPostingFragment (): JobPostingFragment

    @ContributesAndroidInjector
    abstract fun bindSelectLocationFragment (): SelectLocationFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarLocationFragment (): ToolbarLocationFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarCompanyFragment (): ToolbarCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingCompanyFragment (): OnboardingCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindJobTypeFragment (): JobTypeFragment

    @ContributesAndroidInjector
    abstract fun bindShareJobGroupFragment (): ShareJobGroupFragment

    @ContributesAndroidInjector
    abstract fun bindJobSkillsFragment (): JobSkillsFragment

    @ContributesAndroidInjector
    abstract fun bindAllGroupsFragment (): AllGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindTrendingGroupsFragment (): TrendingGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindMyGroupsFragment (): MyGroupsFragment

    @ContributesAndroidInjector(modules = [JobDetailModule::class])
    abstract fun bindJobDescriptionSeekerFragment (): JobDetailFragment

    @ContributesAndroidInjector(modules = [JobDescriptionPosterModule::class])
    abstract fun bindJobDescriptionPosterFragment (): JobDescriptionPosterFragment

    @ContributesAndroidInjector
    abstract fun bindTabJobDescriptionFragment (): TabJobDescriptionFragment

    @ContributesAndroidInjector
    abstract fun bindTabAboutCompanyFragment (): TabAboutCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindSimilarJobListFragment (): SimilarJobListFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplyDialogFragment (): JobApplyDialogFragment

    @ContributesAndroidInjector
    abstract fun bindJobsApplicationsFragment (): JobsApplicationsFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplicantsFragment (): JobApplicantsFragment

    @ContributesAndroidInjector
    abstract fun bindRecentDocsDialogFragment (): RecentDocsDialogFragment

    @ContributesAndroidInjector
    abstract fun bindCreateCompanyFragment (): CreateCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingIndustryFragment (): SelectIndustryOnboardingFragment

    @ContributesAndroidInjector
    abstract fun bindCompanySizesFragment (): CompanySizesFragment

    @ContributesAndroidInjector
    abstract fun bindSimilarJobsFragment (): SimilarJobsFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplySuccessDialogFragment (): JobApplySuccessDialogFragment

    @ContributesAndroidInjector
    abstract fun bindSelectOccupationFragment (): SelectOccupationFragment

    @ContributesAndroidInjector
    abstract fun bindSelectOOIFragment (): SelectOOIFragment

    @ContributesAndroidInjector
    abstract fun bindSelectInterestsFragment (): SelectInterestsFragment

    @ContributesAndroidInjector
    abstract fun bindTabProfileFragment (): TabProfileFragment

    @ContributesAndroidInjector
    abstract fun bindFollowerFragment (): ConnectionsFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarEthnicityFragment (): ToolbarEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarGenderFragment (): ToolbarGenderFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingEthnicityFragment (): OnboardingEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingGenderFragment (): OnboardingGenderFragment

    @ContributesAndroidInjector
    abstract fun bindFollowingFragment (): FollowingFragment

    @ContributesAndroidInjector
    abstract fun bindFollowingGroupsFragment (): FollowingGroupsFragment

    @ContributesAndroidInjector
    abstract fun bindOnboardingAgeFragment (): OnboardingAgeFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarAgeFragment (): ToolbarAgeFragment

//    @ContributesAndroidInjector
//    abstract fun bindDirectMessagesFragment (): ChatsFragment

    @ContributesAndroidInjector
    abstract fun bindNewChatFragment (): NewChatFragment

    @ContributesAndroidInjector(modules = [GroupDetailModule::class])
    abstract fun bindGroupDetailFragment (): GroupDetailFragment

    @ContributesAndroidInjector
    abstract fun bindJobApplicationDialogFragment (): JobApplicationDialogFragment

    @ContributesAndroidInjector
    abstract fun bindCreateGroupFragment (): CreateGroupFragment

    @ContributesAndroidInjector
    abstract fun bindChatFragment (): ChatFragment

    @ContributesAndroidInjector
    abstract fun bindTabAboutGroupDetailFragment (): TabAboutGroupDetailFragment

    @ContributesAndroidInjector
    abstract fun bindGroupConversationFragment (): GroupConversationFragment

    @ContributesAndroidInjector
    abstract fun bindGroupDescriptionFragment (): GroupDescriptionFragment

    @ContributesAndroidInjector
    abstract fun bindProfileSettingsFragment (): ProfileSettingsFragment

    @ContributesAndroidInjector
    abstract fun bindPersonalSettingsFragment (): PersonalSettingsFragment

    @ContributesAndroidInjector
    abstract fun bindInterestsFragment (): InterestsFragment

    @ContributesAndroidInjector
    abstract fun bindChatsFragment (): ChatsFragment

    @ContributesAndroidInjector
    abstract fun bindNewGroupChatFragment (): NewGroupChatFragment

    @ContributesAndroidInjector
    abstract fun bindCreateGroupChatFragment (): CreateGroupChatFragment

    @ContributesAndroidInjector
    abstract fun bindAccountSettingsFragment (): AccountSettingsFragment

    @ContributesAndroidInjector
    abstract fun bindPhoneContactsFragment (): InvitePhoneContactsFragment

    @ContributesAndroidInjector
    abstract fun bindConnectionRequestsFragment(): ConnectionRequestsFragment

    @ContributesAndroidInjector
    abstract fun bindNotificationsFragment(): NotificationsFragment
}