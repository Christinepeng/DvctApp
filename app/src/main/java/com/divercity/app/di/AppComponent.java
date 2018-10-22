package com.divercity.app.di;

import android.app.Application;

import com.divercity.app.DivercityApp;
import com.divercity.app.data.networking.services.ApiModule;
import com.divercity.app.data.networking.services.ApiServiceModule;
import com.divercity.app.di.module.ActivityBindingModule;
import com.divercity.app.di.module.AppModule;
import com.divercity.app.di.module.SharedPreferencesModule;
import com.divercity.app.di.viewmodel.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

/**
 * Created by lucas on 8/31/17.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class,
        ApiModule.class,
        ApiServiceModule.class,
        ViewModelModule.class,
        SharedPreferencesModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(DivercityApp application);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }

}
