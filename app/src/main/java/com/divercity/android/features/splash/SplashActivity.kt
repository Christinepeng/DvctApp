package com.divercity.android.features.splash

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SplashActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, SplashActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = SplashFragment.newInstance()

    public override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }
}
