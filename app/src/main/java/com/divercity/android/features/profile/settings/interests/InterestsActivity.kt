package com.divercity.android.features.profile.settings.interests

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class InterestsActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, InterestsActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = InterestsFragment.newInstance()
}