package com.divercity.app.features.splash

import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class SplashActivity : BaseActivity() {

    override fun fragment(): BaseFragment = SplashFragment.newInstance()
}
