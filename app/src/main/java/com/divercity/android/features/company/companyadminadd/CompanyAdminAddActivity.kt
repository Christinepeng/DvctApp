package com.divercity.android.features.company.companyadminadd

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAdminAddActivity : BaseActivity() {

    companion object {

        private const val INTENT_EXTRA_PARAM_COMPANY_ID = "intentExtraParamCompanyId"

        fun getCallingIntent(
            context: Context,
            companyId: String
        ): Intent {
            val intent = Intent(context, CompanyAdminAddActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_COMPANY_ID, companyId)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
            CompanyAdminAddFragment.newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_COMPANY_ID))

}