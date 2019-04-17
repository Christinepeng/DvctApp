package com.divercity.android.features.groups.deletegroupadmin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteGroupAdminActivity : BaseActivity() {

    companion object {

        const val PARAM_ACTION_TYPE = "paramActionType"

        // EDIT GROUP ADMIN
        const val TYPE_EDIT_GROUP_ADMIN = 5
        const val INTENT_EXTRA_PARAM_GROUP_ID = "intentExtraParamGroupId"
        const val INTENT_EXTRA_PARAM_GROUP_OWNER_ID = "intentExtraParamGroupOwnerId"

        fun getEditGroupAdminBundle(groupId : String, ownerId : String) : Bundle {
            val bundle = Bundle()
            bundle.putInt(
                PARAM_ACTION_TYPE,
                TYPE_EDIT_GROUP_ADMIN
            )
            bundle.putString(INTENT_EXTRA_PARAM_GROUP_ID, groupId)
            bundle.putString(INTENT_EXTRA_PARAM_GROUP_OWNER_ID, ownerId)
            return bundle
        }

        fun getCallingIntent(
            context: Context,
            bundle : Bundle
        ): Intent {
            val intent = Intent(context, DeleteGroupAdminActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        DeleteGroupAdminFragment.newInstance(intent.extras)
}