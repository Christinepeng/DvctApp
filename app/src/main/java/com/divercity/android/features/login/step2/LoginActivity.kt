package com.divercity.android.features.login.step2

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class LoginActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_EMAIL = "intentExtraParamEmail"

        fun getCallingIntent(context: Context, email : String) : Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_EMAIL,email)
            return intent
        }
    }

    override fun fragment(): BaseFragment = LoginFragment.newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_EMAIL))
}
