package com.divercity.android.features.onboarding.personalinfo

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 25/10/2018.
 */

class PersonalInfoActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, PersonalInfoActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = PersonalInfoFragment.newInstance()
}
