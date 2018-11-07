package com.divercity.app.features.jobposting

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 05/11/2018.
 */
 
class JobPostingActivity : BaseActivity(){

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, JobPostingActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = JobPostingFragment.newInstance()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}