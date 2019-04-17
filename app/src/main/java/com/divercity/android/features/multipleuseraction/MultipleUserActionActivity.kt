package com.divercity.android.features.multipleuseraction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class MultipleUserActionActivity : BaseActivity() {

    companion object {

        const val PARAM_ACTION_TYPE = "paramActionType"

        // ADD COMPANY ADMIN
        const val TYPE_ADD_ADMIN_GROUP = 15
        const val INTENT_EXTRA_PARAM_GROUP_ID = "intentExtraParamGroupId"

        fun getAddGroupAdminBundle(groupId : String) : Bundle {
            val bundle = Bundle()
            bundle.putInt(
                PARAM_ACTION_TYPE,
                TYPE_ADD_ADMIN_GROUP
            )
            bundle.putString(INTENT_EXTRA_PARAM_GROUP_ID, groupId)
            return bundle
        }

        fun getCallingIntent(
            context: Context,
            bundle : Bundle
        ): Intent {
            val intent = Intent(context, MultipleUserActionActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        MultipleUserActionFragment.newInstance(intent.extras)
}