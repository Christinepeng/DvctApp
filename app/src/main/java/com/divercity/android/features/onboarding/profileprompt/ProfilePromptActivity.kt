package com.divercity.android.features.onboarding.profileprompt

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ProfilePromptActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, ProfilePromptActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = ProfilePromptFragment.newInstance()
}
