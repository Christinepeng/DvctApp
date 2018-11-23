package com.divercity.app.features.jobs.applicants

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.job.response.JobResponse

class JobApplicantsActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_JOB = "intentExtraParamJob"

        fun getCallingIntent(context: Context, job : JobResponse) : Intent {
            val intent = Intent(context, JobApplicantsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_JOB,job)
            return intent
        }
    }

    override fun fragment(): BaseFragment = JobApplicantsFragment
            .newInstance(intent.getParcelableExtra(INTENT_EXTRA_PARAM_JOB))
}
