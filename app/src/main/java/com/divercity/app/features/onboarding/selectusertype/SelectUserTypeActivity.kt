package com.divercity.app.features.onboarding.selectusertype

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 25/10/2018.
 */

class SelectUserTypeActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, SelectUserTypeActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = SelectUserTypeFragment.newInstance()
}
