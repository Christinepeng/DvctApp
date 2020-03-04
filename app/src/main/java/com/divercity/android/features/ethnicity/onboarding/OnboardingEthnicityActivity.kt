package com.divercity.android.features.ethnicity.onboarding

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 17/10/2018.
 */

class OnboardingEthnicityActivity : BaseActivity() {

    companion object {
        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, OnboardingEthnicityActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = OnboardingEthnicityFragment.newInstance()
}
