package com.divercity.android.features.education.degree

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SelectDegreeActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, SelectDegreeActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        SelectDegreeFragment.newInstance()
}
