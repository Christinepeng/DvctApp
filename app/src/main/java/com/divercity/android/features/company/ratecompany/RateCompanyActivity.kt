package com.divercity.android.features.company.ratecompany

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.company.response.CompanyResponse

class RateCompanyActivity : BaseActivity() {

    companion object {
        private const val PARAM_IS_EDITION = "paramIsEdition"

        fun getCallingIntent(
            context: Context,
            isEdition: Boolean,
            company: CompanyResponse
        ): Intent {
            RateCompanyFragment.DataHolder.data = company
            val intent = Intent(context, RateCompanyActivity::class.java)
            intent.putExtra(PARAM_IS_EDITION, isEdition)
            return intent
        }
    }

    override fun fragment(): BaseFragment = RateCompanyFragment
        .newInstance(intent.getBooleanExtra(PARAM_IS_EDITION, false))
}
