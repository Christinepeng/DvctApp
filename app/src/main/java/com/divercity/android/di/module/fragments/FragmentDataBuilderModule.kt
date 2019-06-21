package com.divercity.android.di.module.fragments

import com.divercity.android.features.agerange.base.SelectAgeFragment
import com.divercity.android.features.agerange.onboarding.OnboardingAgeFragment
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeFragment
import com.divercity.android.features.company.createcompany.CreateCompanyFragment
import com.divercity.android.features.company.selectcompany.base.SelectCompanyFragment
import com.divercity.android.features.company.selectcompany.onboarding.OnboardingCompanyFragment
import com.divercity.android.features.company.selectcompany.withtoolbar.ToolbarCompanyFragment
import com.divercity.android.features.education.degree.SelectDegreeFragment
import com.divercity.android.features.ethnicity.base.SelectEthnicityFragment
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityFragment
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityFragment
import com.divercity.android.features.gender.base.SelectGenderFragment
import com.divercity.android.features.gender.onboarding.OnboardingGenderFragment
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderFragment
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingFragment
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryFragment
import com.divercity.android.features.location.base.SelectLocationFragment
import com.divercity.android.features.location.onboarding.OnboardingLocationFragment
import com.divercity.android.features.location.withtoolbar.ToolbarLocationFragment
import com.divercity.android.features.major.base.SelectMajorFragment
import com.divercity.android.features.major.onboarding.OnboardingMajorFragment
import com.divercity.android.features.major.withtoolbar.SelectSingleMajorFragment
import com.divercity.android.features.school.base.SelectSchoolFragment
import com.divercity.android.features.school.onboarding.OnboardingSchoolFragment
import com.divercity.android.features.school.withtoolbar.SelectSingleSchoolFragment
import com.divercity.android.features.skill.base.SelectSkillFragment
import com.divercity.android.features.skill.editskills.EditUserSkillFragment
import com.divercity.android.features.skill.jobskills.JobSkillsFragment
import com.divercity.android.features.skill.onboarding.OnboardingSkillFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by lucas on 25/10/2018.
 */

@Module
abstract class FragmentDataBuilderModule {

    //COMPANY

    @ContributesAndroidInjector
    abstract fun bindOnboardingCompanyFragment(): OnboardingCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindSelectCompanyFragment(): SelectCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarCompanyFragment(): ToolbarCompanyFragment

    @ContributesAndroidInjector
    abstract fun bindCreateCompanyFragment(): CreateCompanyFragment

    //SCHOOL

    @ContributesAndroidInjector
    abstract fun bindOnboardingSchoolFragment(): OnboardingSchoolFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSchoolFragment(): SelectSchoolFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarSchoolFragment(): SelectSingleSchoolFragment

    //LOCATION

    @ContributesAndroidInjector
    abstract fun bindOnboardingLocationFragment(): OnboardingLocationFragment

    @ContributesAndroidInjector
    abstract fun bindSelectLocationFragment(): SelectLocationFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarLocationFragment(): ToolbarLocationFragment

    //MAJOR

    @ContributesAndroidInjector
    abstract fun bindOnboardingMajorFragment(): OnboardingMajorFragment

    @ContributesAndroidInjector
    abstract fun bindSelectMajorFragment(): SelectMajorFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSingleMajorFragment(): SelectSingleMajorFragment

    //INDUSTRY

    @ContributesAndroidInjector
    abstract fun bindOnboardingIndustryFragment(): SelectIndustryOnboardingFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSingleIndustryFragment(): SelectSingleIndustryFragment

    //ETHNICITY

    @ContributesAndroidInjector
    abstract fun bindOnboardingEthnicityFragment(): OnboardingEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindSelectEthnicityFragment(): SelectEthnicityFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarEthnicityFragment(): ToolbarEthnicityFragment

    //GENDER

    @ContributesAndroidInjector
    abstract fun bindOnboardingGenderFragment(): OnboardingGenderFragment

    @ContributesAndroidInjector
    abstract fun bindSelectGenderFragment(): SelectGenderFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarGenderFragment(): ToolbarGenderFragment

    //AGE

    @ContributesAndroidInjector
    abstract fun bindOnboardingAgeFragment(): OnboardingAgeFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarAgeFragment(): ToolbarAgeFragment

    @ContributesAndroidInjector
    abstract fun bindSelectAgeFragment(): SelectAgeFragment

    //SKILL

    @ContributesAndroidInjector
    abstract fun bindOnboardingSkillFragment(): OnboardingSkillFragment

    @ContributesAndroidInjector
    abstract fun bindJobSkillsFragment(): JobSkillsFragment

    @ContributesAndroidInjector
    abstract fun bindSelectSkillFragment(): SelectSkillFragment

    @ContributesAndroidInjector
    abstract fun bindToolbarSkillFragment(): EditUserSkillFragment

    //DEGREE
    @ContributesAndroidInjector
    abstract fun bindSelectDegreeFragment(): SelectDegreeFragment

}