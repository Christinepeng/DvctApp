package com.divercity.app.features.groups.groupdetail

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class GroupDetailActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_GROUP_ID = "groupId"

        fun getCallingIntent(context: Context?, groupId: String): Intent {
            val intent = Intent(context, GroupDetailActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP_ID, groupId)
            return intent
        }
    }

    override fun fragment(): BaseFragment = GroupDetailFragment.newInstance()
}
