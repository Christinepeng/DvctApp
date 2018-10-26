package com.divercity.app.di

import android.app.Application
import com.divercity.app.DivercityApp
import com.divercity.app.di.module.ActivityBuilderModule
import com.divercity.app.di.module.AppBindsModule
import com.divercity.app.di.module.networking.ApiModule
import com.divercity.app.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

/**
 * Created by lucas on 24/10/2018.
 */

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppBindsModule::class,
        ApiModule::class,
        ViewModelModule::class,
        ActivityBuilderModule::class]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: DivercityApp)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun applicationBind(application: Application): AppComponent.Builder
    }
}