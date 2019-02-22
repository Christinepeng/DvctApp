package com.divercity.android.di.module

import com.divercity.android.features.agerange.onboarding.OnboartdingAgeActivity
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeActivity
import com.divercity.android.features.chat.chat.ChatActivity
import com.divercity.android.features.chat.creategroupchat.CreateGroupChatActivity
import com.divercity.android.features.chat.newchat.NewChatActivity
import com.divercity.android.features.chat.newgroupchat.NewGroupChatActivity
import com.divercity.android.features.chat.recentchats.ChatsActivity
import com.divercity.android.features.company.companysize.CompanySizesActivity
import com.divercity.android.features.company.createcompany.CreateCompanyActivity
import com.divercity.android.features.company.onboarding.OnboardingCompanyActivity
import com.divercity.android.features.company.withtoolbar.ToolbarCompanyActivity
import com.divercity.android.features.contacts.InvitePhoneContactsActivity
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityActivity
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityActivity
import com.divercity.android.features.gender.onboarding.OnboardingGenderActivity
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderActivity
import com.divercity.android.features.groups.answers.AnswerActivity
import com.divercity.android.features.groups.creategroup.step1.CreateGroupActivity
import com.divercity.android.features.groups.creategroup.step3.GroupDescriptionActivity
import com.divercity.android.features.groups.createtopic.CreateTopicActivity
import com.divercity.android.features.groups.followedgroups.FollowingGroupsActivity
import com.divercity.android.features.groups.groupdetail.GroupDetailActivity
import com.divercity.android.features.groups.onboarding.SelectGroupActivity
import com.divercity.android.features.home.HomeActivity
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingActivity
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryActivity
import com.divercity.android.features.jobposting.JobPostingActivity
import com.divercity.android.features.jobposting.jobtype.JobTypeActivity
import com.divercity.android.features.jobposting.sharetogroup.ShareJobGroupActivity
import com.divercity.android.features.jobposting.skills.JobSkillsActivity
import com.divercity.android.features.jobs.applicants.JobApplicantsActivity
import com.divercity.android.features.jobs.description.detail.JobDetailActivity
import com.divercity.android.features.jobs.description.poster.JobDescriptionPosterActivity
import com.divercity.android.features.linkedin.LinkedinActivity
import com.divercity.android.features.location.onboarding.OnboardingLocationActivity
import com.divercity.android.features.location.withtoolbar.ToolbarLocationActivity
import com.divercity.android.features.login.step1.EnterEmailActivity
import com.divercity.android.features.login.step2.LoginActivity
import com.divercity.android.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsActivity
import com.divercity.android.features.onboarding.selectmajor.SelectMajorActivity
import com.divercity.android.features.onboarding.selectoccupation.SelectOccupationActivity
import com.divercity.android.features.onboarding.selectoccupationofinterests.SelectOOIActivity
import com.divercity.android.features.onboarding.selectschool.SelectSchoolActivity
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeActivity
import com.divercity.android.features.profile.settings.ProfileSettingsActivity
import com.divercity.android.features.profile.settings.accountsettings.AccountSettingsActivity
import com.divercity.android.features.profile.settings.interests.InterestsActivity
import com.divercity.android.features.profile.settings.personalsettings.PersonalSettingsActivity
import com.divercity.android.features.signup.SignUpActivity
import com.divercity.android.features.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by lucas on 25/10/2018.
 */

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindEnterEmailActivity(): EnterEmailActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectUserTypeActivity(): SelectUserTypeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindProfilePromptActivity(): ProfilePromptActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectSchoolActivity(): SelectSchoolActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectGroupActivity(): SelectGroupActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectBirthdayActivity(): OnboartdingAgeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectCompanyActivity(): OnboardingCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindOnboardingLocationActivity(): OnboardingLocationActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindOnboardingEthnicityActivity(): OnboardingEthnicityActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindOnboardingGenderActivity(): OnboardingGenderActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectInterestsActivity(): SelectInterestsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectMajorActivity(): SelectMajorActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectOccupationActivity(): SelectOccupationActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindLinkedinActivity(): LinkedinActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobPostingActivity(): JobPostingActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectLocationActivity(): ToolbarLocationActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindToolbarCompanyActivity(): ToolbarCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobTypeActivity(): JobTypeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindShareJobGroupActivity(): ShareJobGroupActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobSkillsActivity(): JobSkillsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobDescriptionSeekerActivity(): JobDetailActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobDescriptionPosterActivity(): JobDescriptionPosterActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobApplicantsActivity(): JobApplicantsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateCompanyActivity(): CreateCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindOnboardingIndustryActivity(): SelectIndustryOnboardingActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindToolbarIndustryActivity(): SelectSingleIndustryActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCompanySizesActivity(): CompanySizesActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectOOIActivity(): SelectOOIActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindToolbarEthnicityActivity(): ToolbarEthnicityActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindToolbarGenderActivity(): ToolbarGenderActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindToolbarAgeActivity(): ToolbarAgeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindDirectMessagesActivity(): ChatsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindNewChatActivity(): NewChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindGroupDetailActivity(): GroupDetailActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindChatActivity(): ChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateGroupActivity(): CreateGroupActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindGroupDescriptionActivity(): GroupDescriptionActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindProfileSettingsActivity(): ProfileSettingsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindPersonalSettingsActivity(): PersonalSettingsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindInterestsActivity(): InterestsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindNewGroupChatActivity(): NewGroupChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateGroupChatActivity(): CreateGroupChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindAccountSettingsActivity(): AccountSettingsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindPhoneContactsActivity(): InvitePhoneContactsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateTopicActivity(): CreateTopicActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindFollowingGroupsActivity(): FollowingGroupsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindAnswerActivity(): AnswerActivity
}