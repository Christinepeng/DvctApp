package com.divercity.app.features.ethnicity.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class ToolbarEthnicityActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ToolbarEthnicityActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        ToolbarEthnicityFragment.newInstance()
}
