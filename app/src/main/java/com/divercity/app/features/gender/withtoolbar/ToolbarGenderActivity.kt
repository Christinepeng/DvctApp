package com.divercity.app.features.gender.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class ToolbarGenderActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ToolbarGenderActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        ToolbarGenderFragment.newInstance()
}
