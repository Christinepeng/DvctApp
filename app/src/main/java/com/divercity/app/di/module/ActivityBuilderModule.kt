package com.divercity.app.di.module

import com.divercity.app.features.home.HomeActivity
import com.divercity.app.features.login.step1.EnterEmailActivity
import com.divercity.app.features.login.step2.LoginActivity
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.app.features.onboarding.selectbirthdate.SelectBirthdayActivity
import com.divercity.app.features.onboarding.selectcompany.SelectCompanyActivity
import com.divercity.app.features.onboarding.selectcountry.SelectCountryActivity
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
    abstract fun bindSelectCompanyActivity(): SelectCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectCountryActivity(): SelectCountryActivity

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
}