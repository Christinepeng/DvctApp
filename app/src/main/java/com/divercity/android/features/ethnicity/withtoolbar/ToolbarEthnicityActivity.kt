package com.divercity.android.features.ethnicity.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ToolbarEthnicityActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ToolbarEthnicityActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        ToolbarEthnicityFragment.newInstance()
}
