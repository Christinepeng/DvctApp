package com.divercity.android.features.groups.followedgroups

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class FollowedGroupsActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, FollowedGroupsActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        FollowedGroupsFragment.newInstance()
}
