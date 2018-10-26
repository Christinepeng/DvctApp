package com.divercity.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.divercity.app.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class DivercityApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
            .builder()
            .applicationBind(this)
            .build()
            .inject(this)
    }
}