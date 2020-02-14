package com.divercity.android.features.linkedin

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment


class LinkedinActivity : BaseActivity() {

    companion object {
        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, LinkedinActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = LinkedinFragment.newInstance()
}
