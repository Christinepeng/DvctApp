package com.divercity.android.features.groups.deletegroupmember

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteGroupMemberActivity : BaseActivity() {

    companion object {

        private const val INTENT_EXTRA_PARAM_GROUP_ID = "intentExtraParamGroupId"

        fun getCallingIntent(
            context: Context,
            groupId : String
        ): Intent {
            val intent = Intent(context, DeleteGroupMemberActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP_ID, groupId)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        DeleteGroupMemberFragment.newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_GROUP_ID))
}