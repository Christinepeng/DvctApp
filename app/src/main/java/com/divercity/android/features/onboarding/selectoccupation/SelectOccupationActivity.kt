package com.divercity.android.features.onboarding.selectoccupation

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 17/10/2018.
 */

class SelectOccupationActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_PROGRESS = "progress"

        fun getCallingIntent(context: Context, progress : Int) : Intent {
            val intent = Intent(context, SelectOccupationActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_PROGRESS,progress)
            return intent
        }
    }

    override fun fragment(): BaseFragment = SelectOccupationFragment.newInstance(
            intent.getIntExtra(INTENT_EXTRA_PARAM_PROGRESS, 0))
}

