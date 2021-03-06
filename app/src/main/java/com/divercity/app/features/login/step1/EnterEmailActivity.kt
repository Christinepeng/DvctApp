package com.divercity.app.features.login.step1

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class EnterEmailActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, EnterEmailActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = EnterEmailFragment.newInstance()
}
