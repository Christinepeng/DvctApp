package com.divercity.android.di.module

import com.divercity.android.services.DivercityMessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by lucas on 25/10/2018.
 */

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract fun divercityMessagingService (): DivercityMessagingService
}