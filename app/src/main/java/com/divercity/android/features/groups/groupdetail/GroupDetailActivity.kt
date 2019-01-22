package com.divercity.android.features.groups.groupdetail

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.group.GroupResponse

class GroupDetailActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_GROUP = "group"

        fun getCallingIntent(context: Context?, group: GroupResponse): Intent {
            val intent = Intent(context, GroupDetailActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP, group)
            return intent
        }
    }

    override fun fragment(): BaseFragment = GroupDetailFragment.newInstance(
            intent.getParcelableExtra(INTENT_EXTRA_PARAM_GROUP))
}
