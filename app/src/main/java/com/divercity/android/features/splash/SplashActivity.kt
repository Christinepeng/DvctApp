package com.divercity.android.features.splash

import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SplashActivity : BaseActivity() {

    override fun fragment(): BaseFragment = SplashFragment.newInstance()

    public override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }
}
