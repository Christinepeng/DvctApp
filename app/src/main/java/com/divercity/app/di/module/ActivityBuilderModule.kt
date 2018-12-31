package com.divercity.app.di.module

import com.divercity.app.features.agerange.onboarding.OnboartdingAgeActivity
import com.divercity.app.features.agerange.withtoolbar.ToolbarAgeActivity
import com.divercity.app.features.chat.chat.ChatActivity
import com.divercity.app.features.chat.chatlist.ChatsActivity
import com.divercity.app.features.chat.newchat.NewChatActivity
import com.divercity.app.features.company.companysize.CompanySizesActivity
import com.divercity.app.features.company.createcompany.CreateCompanyActivity
import com.divercity.app.features.company.onboarding.OnboardingCompanyActivity
import com.divercity.app.features.company.withtoolbar.ToolbarCompanyActivity
import com.divercity.app.features.ethnicity.onboarding.OnboardingEthnicityActivity
import com.divercity.app.features.ethnicity.withtoolbar.ToolbarEthnicityActivity
import com.divercity.app.features.gender.onboarding.OnboardingGenderActivity
import com.divercity.app.features.gender.withtoolbar.ToolbarGenderActivity
import com.divercity.app.features.groups.creategroup.step1.CreateGroupActivity
import com.divercity.app.features.groups.groupdetail.GroupDetailActivity
import com.divercity.app.features.groups.onboarding.SelectGroupActivity
import com.divercity.app.features.home.HomeActivity
import com.divercity.app.features.industry.onboarding.SelectIndustryOnboardingActivity
import com.divercity.app.features.industry.selectsingleindustry.SelectSingleIndustryActivity
import com.divercity.app.features.jobposting.JobPostingActivity
import com.divercity.app.features.jobposting.jobtype.JobTypeActivity
import com.divercity.app.features.jobposting.sharetogroup.ShareJobGroupActivity
import com.divercity.app.features.jobposting.skills.JobSkillsActivity
import com.divercity.app.features.jobs.applicants.JobApplicantsActivity
import com.divercity.app.features.jobs.description.detail.JobDetailActivity
import com.divercity.app.features.jobs.description.poster.JobDescriptionPosterActivity
import com.divercity.app.features.linkedin.LinkedinActivity
import com.divercity.app.features.location.onboarding.OnboardingLocationActivity
import com.divercity.app.features.location.withtoolbar.ToolbarLocationActivity
import com.divercity.app.features.login.step1.EnterEmailActivity
import com.divercity.app.features.login.step2.LoginActivity
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.app.features.onboarding.selectinterests.SelectInterestsActivity
import com.divercity.app.features.onboarding.selectmajor.SelectMajorActivity
import com.divercity.app.features.onboarding.selectoccupation.SelectOccupationActivity
import com.divercity.app.features.onboarding.selectoccupationofinterests.SelectOOIActivity
import com.divercity.app.features.onboarding.selectschool.SelectSchoolActivity
import com.divercity.app.features.onboarding.selectusertype.SelectUserTypeActivity
import com.divercity.app.features.signup.SignUpActivity
import com.divercity.app.features.splash.SplashActivity
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
}