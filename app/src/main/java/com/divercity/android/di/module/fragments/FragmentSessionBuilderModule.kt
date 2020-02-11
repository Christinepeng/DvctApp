package com.divercity.android.di.module.fragments

import com.divercity.android.features.login.step1.*
import com.divercity.android.features.login.step2.LoginFragment
import com.divercity.android.features.signup.SignUpFragment
import com.divercity.android.features.signup.SignUpPageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by lucas on 25/10/2018.
 */

@Module
abstract class FragmentSessionBuilderModule {

    @ContributesAndroidInjector
    abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun bindSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun bindLogInPageFragment(): LogInPageFragment

    @ContributesAndroidInjector
    abstract fun bindSignUpPageFragment(): SignUpPageFragment

    @ContributesAndroidInjector
    abstract fun bindGetStartedFragment(): GetStartedFragment

    @ContributesAndroidInjector
    abstract fun bindGetStartedJoinCommunitiesFragment(): GetStartedJoinCommunitiesFragment

    @ContributesAndroidInjector
    abstract fun bindGetStartedFindJobsFragment(): GetStartedFindJobsFragment

    @ContributesAndroidInjector
    abstract fun bindGetStartedRecruitFragment(): GetStartedRecruitFragment

}