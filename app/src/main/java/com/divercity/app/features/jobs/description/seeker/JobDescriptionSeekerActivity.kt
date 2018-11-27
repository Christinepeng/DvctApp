package com.divercity.app.features.jobs.description.seeker

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class JobDescriptionSeekerActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_JOB_ID = "intentExtraParamJob"

        fun getCallingIntent(context: Context, jobId : String?) : Intent {
            val intent = Intent(context, JobDescriptionSeekerActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_JOB_ID,jobId)
            return intent
        }
    }

    override fun fragment(): BaseFragment = JobDescriptionSeekerFragment
            .newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_JOB_ID))
}
