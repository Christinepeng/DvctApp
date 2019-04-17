package com.divercity.android.features.company.selectcompany.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ToolbarCompanyActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ToolbarCompanyActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        ToolbarCompanyFragment.newInstance()
}