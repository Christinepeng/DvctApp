package com.divercity.android.features.profile.settings.personalsettings

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class PersonalSettingsActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, PersonalSettingsActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = PersonalSettingsFragment.newInstance()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}
