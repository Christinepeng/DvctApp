package com.divercity.android.di.module.fragments

import com.divercity.android.features.login.step1.EnterEmailFragment
import com.divercity.android.features.login.step2.LoginFragment
import com.divercity.android.features.signup.SignUpFragment
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
    abstract fun bindEnterEmailFragment(): EnterEmailFragment

}