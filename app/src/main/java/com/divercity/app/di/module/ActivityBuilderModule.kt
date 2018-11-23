package com.divercity.app.di.module

import com.divercity.app.features.company.onboarding.OnboardingCompanyActivity
import com.divercity.app.features.company.withtoolbar.ToolbarCompanyActivity
import com.divercity.app.features.home.HomeActivity
import com.divercity.app.features.jobposting.JobPostingActivity
import com.divercity.app.features.jobposting.jobtype.JobTypeActivity
import com.divercity.app.features.jobposting.sharetogroup.ShareJobGroupActivity
import com.divercity.app.features.jobposting.skills.JobSkillsActivity
import com.divercity.app.features.jobs.applicants.JobApplicantsActivity
import com.divercity.app.features.jobs.description.poster.JobDescriptionPosterActivity
import com.divercity.app.features.jobs.description.seeker.JobDescriptionSeekerActivity
import com.divercity.app.features.linkedin.LinkedinActivity
import com.divercity.app.features.location.onboarding.OnboardingLocationActivity
import com.divercity.app.features.location.withtoolbar.ToolbarLocationActivity
import com.divercity.app.features.login.step1.EnterEmailActivity
import com.divercity.app.features.login.step2.LoginActivity
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.app.features.onboarding.selectbirthdate.SelectBirthdayActivity
import com.divercity.app.features.onboarding.selectethnicity.SelectEthnicityActivity
import com.divercity.app.features.onboarding.selectgender.SelectGenderActivity
import com.divercity.app.features.onboarding.selectgroups.SelectGroupActivity
import com.divercity.app.features.onboarding.selectindustry.SelectIndustryActivity
import com.divercity.app.features.onboarding.selectinterests.SelectInterestsActivity
import com.divercity.app.features.onboarding.selectmajor.SelectMajorActivity
import com.divercity.app.features.onboarding.selectoccupation.SelectOccupationActivity
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
    abstract fun bindSelectBirthdayActivity(): SelectBirthdayActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectCompanyActivity(): OnboardingCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindOnboardingLocationActivity(): OnboardingLocationActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectEthnicityActivity(): SelectEthnicityActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectGenderActivity(): SelectGenderActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectIndustryActivity(): SelectIndustryActivity

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
    abstract fun bindJobDescriptionSeekerActivity(): JobDescriptionSeekerActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobDescriptionPosterActivity(): JobDescriptionPosterActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobApplicantsActivity(): JobApplicantsActivity
}