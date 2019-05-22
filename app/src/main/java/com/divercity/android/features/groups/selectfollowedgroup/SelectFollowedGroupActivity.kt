package com.divercity.android.features.groups.selectfollowedgroup

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SelectFollowedGroupActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, SelectFollowedGroupActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
        SelectFollowedGroupFragment.newInstance()
}
