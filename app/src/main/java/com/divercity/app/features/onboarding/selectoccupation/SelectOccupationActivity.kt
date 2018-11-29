package com.divercity.app.features.onboarding.selectoccupation

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.features.onboarding.selectschool.SelectSchoolFragment

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

//    TODO Create fragment select occupation of interests
    override fun fragment(): BaseFragment = SelectSchoolFragment.newInstance(
            intent.getIntExtra(INTENT_EXTRA_PARAM_PROGRESS, 0))
}

