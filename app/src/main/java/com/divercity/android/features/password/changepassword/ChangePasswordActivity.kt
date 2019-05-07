package com.divercity.android.features.password.changepassword

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ChangePasswordActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ChangePasswordActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        ChangePasswordFragment.newInstance()
}
