package com.divercity.android.di

import android.app.Application
import com.divercity.android.DivercityApp
import com.divercity.android.db.RoomModule
import com.divercity.android.di.module.ActivityBuilderModule
import com.divercity.android.di.module.AppBindsModule
import com.divercity.android.di.module.ServiceBuilderModule
import com.divercity.android.di.module.networking.ApiModule
import com.divercity.android.di.module.networking.apollo.ApiApolloModule
import com.divercity.android.di.viewmodel.ViewModelModule
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
        ApiApolloModule::class,
        ViewModelModule::class,
        ActivityBuilderModule::class,
        ServiceBuilderModule::class,
        RoomModule::class]
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