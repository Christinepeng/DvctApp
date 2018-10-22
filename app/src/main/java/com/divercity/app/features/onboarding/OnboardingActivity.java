package com.divercity.app.features.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseActivity;

import androidx.navigation.Navigation;

/**
 * Created by lucas on 03/10/2018.
 */

public class OnboardingActivity extends BaseActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, OnboardingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_container_onboarding).navigateUp();
    }

}