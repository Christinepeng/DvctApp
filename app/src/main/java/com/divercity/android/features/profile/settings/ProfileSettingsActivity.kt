package com.divercity.android.features.profile.settings

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ProfileSettingsActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_EMAIL = "intentExtraParamEmail"

        fun getCallingIntent(context: Context, email : String) : Intent {
            val intent = Intent(context, ProfileSettingsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_EMAIL,email)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ProfileSettingsFragment.newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_EMAIL))

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}
