package com.divercity.app.features.industry.onboarding

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 17/10/2018.
 */

class SelectIndustryOnboardingActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_PROGRESS = "progress"

        fun getCallingIntent(context: Context, progress : Int) : Intent {
            val intent = Intent(context, SelectIndustryOnboardingActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_PROGRESS,progress)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        SelectIndustryOnboardingFragment.newInstance(
            intent.getIntExtra(
                    INTENT_EXTRA_PARAM_PROGRESS,
                0
            )
        )
}
