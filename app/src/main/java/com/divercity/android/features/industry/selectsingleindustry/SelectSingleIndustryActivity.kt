package com.divercity.android.features.industry.selectsingleindustry

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SelectSingleIndustryActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, SelectSingleIndustryActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        SelectSingleIndustryFragment.newInstance()
}
