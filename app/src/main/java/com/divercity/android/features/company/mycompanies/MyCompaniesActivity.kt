package com.divercity.android.features.company.mycompanies

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 17/10/2018.
 */

class MyCompaniesActivity  : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, MyCompaniesActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = MyCompaniesFragment.newInstance()
}
