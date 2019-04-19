package com.divercity.android.features.company.deleteadmincompany

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteCompanyAdminActivity : BaseActivity() {

    companion object {

        const val INTENT_EXTRA_PARAM_COMPANY_ID = "intentExtraParamCompanyId"
        const val INTENT_EXTRA_PARAM_COMPANY_OWNER_ID = "intentExtraParamCompanyOwnerId"

        fun getCallingIntent(
            context: Context,
            companyId : String,
            ownerId: String
        ): Intent {
            val intent = Intent(context, DeleteCompanyAdminActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_COMPANY_ID, companyId)
            intent.putExtra(INTENT_EXTRA_PARAM_COMPANY_OWNER_ID, ownerId)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        DeleteCompanyAdminFragment.newInstance(intent.extras)
}