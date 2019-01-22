package com.divercity.android.features.splash

import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SplashActivity : BaseActivity() {

    override fun fragment(): BaseFragment = SplashFragment.newInstance()
}
