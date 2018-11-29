package com.divercity.app.features.industry.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class ToolbarIndustryActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ToolbarIndustryActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        ToolbarIndustryFragment.newInstance()
}
