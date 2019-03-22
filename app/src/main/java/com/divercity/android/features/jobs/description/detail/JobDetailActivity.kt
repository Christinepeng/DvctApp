package com.divercity.android.features.jobs.description.detail

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.job.response.JobResponse

class JobDetailActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_JOB_ID = "intentExtraParamJob"

        fun getCallingIntent(
            context: Context,
            jobId: String?,
            job: JobResponse?): Intent {
            JobDetailFragment.DataHolder.data = job
            val intent = Intent(context, JobDetailActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_JOB_ID, jobId)
            return intent
        }
    }

    override fun fragment(): BaseFragment = JobDetailFragment
        .newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_JOB_ID))
}
