package com.divercity.app.features.linkedin

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment




class LinkedinActivity : BaseActivity() {

    companion object {
        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, LinkedinActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = LinkedinFragment.newInstance()
}
