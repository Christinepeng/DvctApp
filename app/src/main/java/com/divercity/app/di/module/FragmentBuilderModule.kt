package com.divercity.app.di.module

import com.divercity.app.features.home.groups.GroupsFragment
import com.divercity.app.features.home.home.HomeFragment
import com.divercity.app.features.home.jobs.JobsFragment
import com.divercity.app.features.home.jobs.jobs.JobsListFragment
import com.divercity.app.features.home.jobs.module.JobsModule
import com.divercity.app.features.home.jobs.mypostings.MyPostingsFragment
import com.divercity.app.features.home.jobs.saved.SavedJobsFragment
import com.divercity.app.features.home.notifications.NotificationsFragment
import com.divercity.app.features.home.profile.ProfileFragment
import com.divercity.app.features.home.settings.SettingsFragment
import com.divercity.app.features.jobposting.JobPostingFragment
import com.divercity.app.features.linkedin.LinkedinFragment
import com.divercity.app.features.location.SelectLocationFragment
import com.divercity.app.features.location.ToolbarLocationFragment
import com.divercity.app.features.login.step1.EnterEmailFragment
import com.divercity.app.features.login.step2.LoginFragment
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptFragment
import com.divercity.app.features.onboarding.selectbirthdate.SelectBirthdayFragment
import com.divercity.app.features.onboarding.selectcompany.SelectCompanyFragment
import com.divercity.app.features.onboarding.selectcountry.SelectCountryFragment
import com.divercity.app.features.onboarding.selectethnicity.SelectEthnicityFragment
import com.divercity.app.features.onboarding.selectgender.SelectGenderFragment
import com.divercity.app.features.onboarding.selectgroups.SelectGroupFragment
import com.divercity.app.features.onboarding.selectindustry.SelectIndustryFragment
import com.divercity.app.features.onboarding.selectmajor.SelectMajorFragment
import com.divercity.app.features.onboarding.selectschool.SelectSchoolFragment
import com.divercity.app.features.onboarding.selectusertype.SelectUserTypeFragment
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

    @ContributesAndroidInjector
    abstract fun bindGroupsFragment (): GroupsFragment

    @ContributesAndroidInjector
    abstract fun bindHomeFragment (): HomeFragment

    @ContributesAndroidInjector(modules = [JobsModule::class])
    abstract fun bindJobsFragment (): JobsFragment

    @ContributesAndroidInjector
    abstract fun bindNotificationsFragment (): NotificationsFragment

    @ContributesAndroidInjector
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
    abstract fun bindSelectBirthdayFragment (): SelectBirthdayFragment

    @ContributesAndroidInjector
    abstract fun bindSelectCompanyFragment (): SelectCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindSelectCountryFragment (): SelectCountryFragment

    @ContributesAndroidInjector
    abstract fun bindSelectEthnicityFragment (): SelectEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindSelectGenderFragment (): SelectGenderFragment

    @ContributesAndroidInjector
    abstract fun bindSelectIndustryFragment (): SelectIndustryFragment

    @ContributesAndroidInjector
    abstract fun bindSelectMajorFragment (): SelectMajorFragment

    @ContributesAndroidInjector
    abstract fun bindSelectGroupFragment (): SelectGroupFragment

    @ContributesAndroidInjector
    abstract fun bindJobsListFragment (): JobsListFragment

    @ContributesAndroidInjector
    abstract fun bindMyPostingsFragment (): MyPostingsFragment

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
}