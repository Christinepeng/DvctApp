package com.divercity.app.core.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.divercity.app.R;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by lucas on 15/10/2018.
 */

public abstract class BaseFragmentActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        addFragment(savedInstanceState);
    }

    private void addFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment()).commit();
    }

    protected abstract BaseFragment fragment();

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

}
