package com.divercity.android.features.login.step1

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class GetStartedActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, GetStartedActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = GetStartedFragment.newInstance()
}
