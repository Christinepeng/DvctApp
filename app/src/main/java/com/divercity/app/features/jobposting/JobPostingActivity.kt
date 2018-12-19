package com.divercity.app.features.jobposting

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.job.response.JobResponse

/**
 * Created by lucas on 05/11/2018.
 */
 
class JobPostingActivity : BaseActivity(){

    companion object {
        private const val INTENT_EXTRA_PARAM_JOB = "intentExtraParamJob"

        fun getCallingIntent(context: Context?, job : JobResponse?) : Intent {
            val intent = Intent(context, JobPostingActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_JOB,job)
            return intent
        }
    }

    override fun fragment(): BaseFragment = JobPostingFragment.newInstance(intent.getParcelableExtra(
        JobPostingActivity.INTENT_EXTRA_PARAM_JOB
    ))
}