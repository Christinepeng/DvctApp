package com.divercity.android.features.singleuseraction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class SingleUserActionActivity : BaseActivity() {

    companion object {

        const val PARAM_ACTION_TYPE = "paramActionType"

        // ADD COMPANY ADMIN
        const val TYPE_ADD_ADMIN = 5
        private const val INTENT_EXTRA_PARAM_COMPANY_ID = "intentExtraParamCompanyId"

        fun getAddAdminBundle(companyId : String) : Bundle {
            val bundle = Bundle()
            bundle.putInt(
                PARAM_ACTION_TYPE,
                TYPE_ADD_ADMIN
            )
            bundle.putString(INTENT_EXTRA_PARAM_COMPANY_ID, companyId)
            return bundle
        }

        // SHARE JOB VIA MESSAGE
        const val TYPE_SHARE_JOB = 10
        const val INTENT_EXTRA_PARAM_JOB_ID = "intentExtraParamJobId"

        fun getShareJobViaMessageBundle(jobId : String) : Bundle {
            val bundle = Bundle()
            bundle.putInt(
                PARAM_ACTION_TYPE,
                TYPE_SHARE_JOB
            )
            bundle.putString(INTENT_EXTRA_PARAM_JOB_ID, jobId)
            return bundle
        }

        fun getCallingIntent(
            context: Context,
            bundle : Bundle
        ): Intent {
            val intent = Intent(context, SingleUserActionActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        SingleUserActionFragment.newInstance(intent.extras)
}