package com.divercity.app.features.jobposting.sharetogroup

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class ShareJobGroupActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_PROGRESS = "progress"

        fun getCallingIntent(context: Context, progress : Int) : Intent {
            val intent = Intent(context, ShareJobGroupActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_PROGRESS,progress)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ShareJobGroupFragment.newInstance(
            intent.getIntExtra(INTENT_EXTRA_PARAM_PROGRESS, 0))
}
