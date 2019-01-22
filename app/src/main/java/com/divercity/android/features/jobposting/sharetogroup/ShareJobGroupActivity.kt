package com.divercity.android.features.jobposting.sharetogroup

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ShareJobGroupActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_JOB_ID = "extraParamJobId"

        fun getCallingIntent(context: Context?, jobId : String?) : Intent {
            val intent = Intent(context, ShareJobGroupActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_JOB_ID,jobId)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ShareJobGroupFragment.newInstance(
            intent.getStringExtra(INTENT_EXTRA_PARAM_JOB_ID))
}
