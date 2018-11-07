package com.divercity.app.features.jobposting.jobtype

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 17/10/2018.
 */

class JobTypeActivity  : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, JobTypeActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = JobTypeFragment.newInstance()
}
