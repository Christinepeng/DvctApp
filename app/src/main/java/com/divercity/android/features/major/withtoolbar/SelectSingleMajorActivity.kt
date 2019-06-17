package com.divercity.android.features.major.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SelectSingleMajorActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, SelectSingleMajorActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        SelectSingleMajorFragment.newInstance()
}
