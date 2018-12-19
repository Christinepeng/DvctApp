package com.divercity.app.features.ethnicity.onboarding

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.features.ethnicity.base.SelectEthnicityFragment

/**
 * Created by lucas on 17/10/2018.
 */

class OnboardingEthnicityActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_PROGRESS = "progress"

        fun getCallingIntent(context: Context, progress : Int) : Intent {
            val intent = Intent(context, OnboardingEthnicityActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_PROGRESS,progress)
            return intent
        }
    }

    override fun fragment(): BaseFragment = SelectEthnicityFragment.newInstance(
            intent.getIntExtra(INTENT_EXTRA_PARAM_PROGRESS, 0))
}
