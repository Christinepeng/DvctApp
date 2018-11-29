package com.divercity.app.features.company.createcompany

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 05/11/2018.
 */
 
class CreateCompanyActivity : BaseActivity(){

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, CreateCompanyActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = CreateCompanyFragment.newInstance()
}