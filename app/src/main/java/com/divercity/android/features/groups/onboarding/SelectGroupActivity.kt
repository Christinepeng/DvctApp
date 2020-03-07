package com.divercity.android.features.groups.onboarding

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SelectGroupActivity : BaseActivity() {

    companion object {
        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, SelectGroupActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = SelectGroupFragment.newInstance()
}
