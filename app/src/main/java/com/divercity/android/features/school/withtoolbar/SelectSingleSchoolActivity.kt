package com.divercity.android.features.school.withtoolbar

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SelectSingleSchoolActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, SelectSingleSchoolActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        SelectSingleSchoolFragment.newInstance()
}
