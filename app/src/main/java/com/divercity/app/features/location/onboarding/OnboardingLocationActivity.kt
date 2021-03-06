package com.divercity.app.features.location.onboarding

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 17/10/2018.
 */

class OnboardingLocationActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_PROGRESS = "progress"

        fun getCallingIntent(context: Context, progress : Int) : Intent {
            val intent = Intent(context, OnboardingLocationActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_PROGRESS,progress)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        OnboardingLocationFragment.newInstance(
            intent.getIntExtra(
                    INTENT_EXTRA_PARAM_PROGRESS,
                0
            )
        )
}
