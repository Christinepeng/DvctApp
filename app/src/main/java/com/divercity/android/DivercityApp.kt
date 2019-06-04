package com.divercity.android

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.multidex.MultiDex
import com.divercity.android.di.DaggerAppComponent
import com.divercity.android.helpers.NotificationHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import io.branch.referral.Branch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class DivercityApp : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // Branch object initialization
        Branch.getAutoInstance(this)

//        LeakCanary.install(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            // Branch logging for debugging
            Branch.enableDebugMode()
        }

        DaggerAppComponent
            .builder()
            .applicationBind(this)
            .build()
            .inject(this)

        notificationHelper.cancellAllNotifications()
    }
}