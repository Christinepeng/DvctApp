package com.divercity.app.di.module

import com.divercity.app.features.agerange.base.SelectAgeFragment
import com.divercity.app.features.agerange.onboarding.OnboardingAgeFragment
import com.divercity.app.features.agerange.withtoolbar.ToolbarAgeFragment
import com.divercity.app.features.chat.chat.ChatFragment
import com.divercity.app.features.chat.newchat.NewChatFragment
import com.divercity.app.features.chat.recentchats.ChatsFragment
import com.divercity.app.features.company.base.SelectCompanyFragment
import com.divercity.app.features.company.companysize.CompanySizesFragment
import com.divercity.app.features.company.createcompany.CreateCompanyFragment
import com.divercity.app.features.company.onboarding.OnboardingCompanyFragment
import com.divercity.app.features.company.withtoolbar.ToolbarCompanyFragment
import com.divercity.app.features.dialogs.jobapplication.JobApplicationDialogFragment
import com.divercity.app.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.app.features.dialogs.jobapplysuccess.JobApplySuccessDialogFragment
import com.divercity.app.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.app.features.ethnicity.base.SelectEthnicityFragment
import com.divercity.app.features.ethnicity.onboarding.OnboardingEthnicityFragment
import com.divercity.app.features.ethnicity.withtoolbar.ToolbarEthnicityFragment
import com.divercity.app.features.gender.base.SelectGenderFragment
import com.divercity.app.features.gender.onboarding.OnboardingGenderFragment
import com.divercity.app.features.gender.withtoolbar.ToolbarGenderFragment
import com.divercity.app.features.groups.TabGroupsFragment
import com.divercity.app.features.groups.all.AllGroupsFragment
import com.divercity.app.features.groups.creategroup.step1.CreateGroupFragment
import com.divercity.app.features.groups.groupdetail.GroupDetailFragment
import com.divercity.app.features.groups.groupdetail.about.TabAboutGroupDetailFragment
import com.divercity.app.features.groups.groupdetail.conversation.GroupConversationFragment
import com.divercity.app.features.groups.groupdetail.module.GroupDetailModule
import com.divercity.app.features.groups.module.GroupsModule
import com.divercity.app.features.groups.mygroups.MyGroupsFragment
import com.divercity.app.features.groups.onboarding.SelectGroupFragment
import com.divercity.app.features.groups.trending.TrendingGroupsFragment
import com.divercity.app.features.home.home.HomeFragment
import com.divercity.app.features.home.notifications.NotificationsFragment
import com.divercity.app.features.home.settings.SettingsFragment
import com.divercity.app.features.industry.onboarding.SelectIndustryOnboardingFragment
import com.divercity.app.features.industry.selectsingleindustry.SelectSingleIndustryFragment
import com.divercity.app.features.jobposting.JobPostingFragment
import com.divercity.app.features.jobposting.jobtype.JobTypeFragment
import com.divercity.app.features.jobposting.sharetogroup.ShareJobGroupFragment
import com.divercity.app.features.jobposting.skills.JobSkillsFragment
import com.divercity.app.features.jobs.TabJobsFragment
import com.divercity.app.features.jobs.applicants.JobApplicantsFragment
import com.divercity.app.features.jobs.applications.JobsApplicationsFragment
import com.divercity.app.features.jobs.description.aboutcompany.TabAboutCompanyFragment
import com.divercity.app.features.jobs.description.detail.JobDetailFragment
import com.divercity.app.features.jobs.description.detail.module.JobDetailModule
import com.divercity.app.features.jobs.description.jobdescription.TabJobDescriptionFragment
import com.divercity.app.features.jobs.description.poster.JobDescriptionPosterFragment
import com.divercity.app.features.jobs.description.poster.module.JobDescriptionPosterModule
import com.divercity.app.features.jobs.description.poster.similarjobs.SimilarJobsFragment
import com.divercity.app.features.jobs.jobs.JobsListFragment
import com.divercity.app.features.jobs.module.JobsModule
import com.divercity.app.features.jobs.mypostings.MyJobsPostingsFragment
import com.divercity.app.features.jobs.savedjobs.SavedJobsFragment
import com.divercity.app.features.jobs.similarjobs.SimilarJobListFragment
import com.divercity.app.features.linkedin.LinkedinFragment
import com.divercity.app.features.location.base.SelectLocationFragment
import com.divercity.app.features.location.onboarding.OnboardingLocationFragment
import com.divercity.app.features.location.withtoolbar.ToolbarLocationFragment
import com.divercity.app.features.login.step1.EnterEmailFragment
import com.divercity.app.features.login.step2.LoginFragment
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptFragment
import com.divercity.app.features.onboarding.selectinterests.SelectInterestsFragment
import com.divercity.app.features.onboarding.selectmajor.SelectMajorFragment
import com.divercity.app.features.onboarding.selectoccupation.SelectOccupationFragment
import com.divercity.app.features.onboarding.selectoccupationofinterests.SelectOOIFragment
import com.divercity.app.features.onboarding.selectschool.SelectSchoolFragment
import com.divercity.app.features.onboarding.selectusertype.SelectUserTypeFragment
import com.divercity.app.features.profile.ProfileFragment
import com.divercity.app.features.profile.module.ProfileModule
import com.divercity.app.features.profile.tabfollowers.FollowerFragment
import com.divercity.app.features.profile.tabfollowing.FollowingFragment
import com.divercity.app.features.profile.tabgroups.FollowingGroupsFragment
import com.divercity.app.features.profile.tabprofile.TabProfileFragment
import com.divercity.app.features.signup.SignUpFragment
import com.divercity.app.features.splash.SplashFragment
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

    @ContributesAndroidInjector
    abstract fun bindNotificationsFragment (): NotificationsFragment

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
    abstract fun bindFollowerFragment (): FollowerFragment

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

    @ContributesAndroidInjector
    abstract fun bindDirectMessagesFragment (): ChatsFragment

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
}