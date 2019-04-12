package com.divercity.android.features.company.companyaddadmin

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAddAdminActivity : BaseActivity() {

    companion object {

        const val PARAM_COMPANY_ID = "paramCompanyId"

        fun getCallingIntent(
            context: Context,
            companyId : String
        ): Intent {
            val intent = Intent(context, CompanyAddAdminActivity::class.java)
            intent.putExtra(PARAM_COMPANY_ID, companyId)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        CompanyAddAdminFragment
            .newInstance(intent.getStringExtra(PARAM_COMPANY_ID))
}