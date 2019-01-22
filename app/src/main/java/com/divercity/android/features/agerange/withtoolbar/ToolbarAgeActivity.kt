package com.divercity.android.features.agerange.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ToolbarAgeActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, ToolbarAgeActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
            ToolbarAgeFragment.newInstance()
}
