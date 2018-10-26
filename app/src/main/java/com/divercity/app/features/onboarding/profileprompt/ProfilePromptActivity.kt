package com.divercity.app.features.onboarding.profileprompt

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class ProfilePromptActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, ProfilePromptActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = ProfilePromptFragment.newInstance()
}
