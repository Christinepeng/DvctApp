package com.divercity.app.features.agerange.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class ToolbarAgeActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, ToolbarAgeActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
            ToolbarAgeFragment.newInstance()
}
