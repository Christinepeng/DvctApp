package com.divercity.app.features.groups.creategroup.step1

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class CreateGroupActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return  Intent(context, CreateGroupActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = CreateGroupFragment.newInstance()
}
