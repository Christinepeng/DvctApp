package com.divercity.android.features.company.ratecompany

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.company.response.CompanyResponse

class RateCompanyActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_COMPANY_ID = "intentExtraParamJob"

        fun getCallingIntent(
            context: Context,
            companyId: String?,
            company: CompanyResponse?
        ): Intent {
            RateCompanyFragment.DataHolder.data = company
            val intent = Intent(context, RateCompanyActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_COMPANY_ID, companyId)
            return intent
        }
    }

    override fun fragment(): BaseFragment = RateCompanyFragment
        .newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_COMPANY_ID))
}
