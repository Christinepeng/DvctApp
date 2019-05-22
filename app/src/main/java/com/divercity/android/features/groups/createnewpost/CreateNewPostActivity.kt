package com.divercity.android.features.groups.createnewpost

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class CreateNewPostActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, CreateNewPostActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = CreateNewPostFragment.newInstance()
}
