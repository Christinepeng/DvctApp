package com.divercity.app.features.splash;

import android.os.Bundle;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseActivity;

import androidx.navigation.Navigation;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.container).navigateUp();
    }

}
