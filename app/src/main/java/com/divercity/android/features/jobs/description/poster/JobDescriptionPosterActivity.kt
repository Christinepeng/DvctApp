package com.divercity.android.features.jobs.description.poster

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.job.response.JobResponse

class JobDescriptionPosterActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_JOB = "intentExtraParamJob"

        fun getCallingIntent(context: Context?, job : JobResponse) : Intent {
            val intent = Intent(context, JobDescriptionPosterActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_JOB,job)
            return intent
        }
    }

    override fun fragment(): BaseFragment = JobDescriptionPosterFragment
            .newInstance(intent.getParcelableExtra(INTENT_EXTRA_PARAM_JOB))
}
