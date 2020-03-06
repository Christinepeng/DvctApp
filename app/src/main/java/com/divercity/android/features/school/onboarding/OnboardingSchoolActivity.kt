package com.divercity.android.features.school.onboarding

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 17/10/2018.
 */

class OnboardingSchoolActivity : BaseActivity() {

    companion object {
        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, OnboardingSchoolActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = OnboardingSchoolFragment.newInstance()
}
