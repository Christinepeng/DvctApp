package com.divercity.android.features.groups.followedgroups

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class FollowingGroupsActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, FollowingGroupsActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        FollowingGroupsFragment.newInstance()
}
