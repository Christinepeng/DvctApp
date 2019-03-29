package com.divercity.android.features.groups.groupdetail

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.group.group.GroupResponse

class GroupDetailActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_GROUP_ID = "groupId"

        fun getCallingIntent(context: Context?, groupId: String, group: GroupResponse?): Intent {
            GroupDetailFragment.DataHolder.data = group
            val intent = Intent(context, GroupDetailActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP_ID, groupId)
            return intent
        }
    }

    override fun fragment(): BaseFragment = GroupDetailFragment.newInstance(
        intent.getStringExtra(INTENT_EXTRA_PARAM_GROUP_ID)
    )

    override fun onBackPressed() {
        if (isTaskRoot)
            navigator.navigateToHomeActivity(this)
        super.onBackPressed()
    }
}
