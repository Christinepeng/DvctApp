package com.divercity.android.features.signup

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SignUpPageActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, SignUpPageActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = SignUpPageFragment.newInstance()
}
