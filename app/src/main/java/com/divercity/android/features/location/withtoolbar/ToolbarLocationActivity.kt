package com.divercity.android.features.location.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ToolbarLocationActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_CALLED_FROM = "calledFrom"

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ToolbarLocationActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        ToolbarLocationFragment.newInstance()
}
