package com.divercity.android.features.password.resetpassword

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class ResetPasswordActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_TOKEN = "intentExtraParamToken"

        fun getCallingIntent(context: Context?, token : String) : Intent {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_TOKEN,token)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        ResetPasswordFragment.newInstance(
            intent.getStringExtra(INTENT_EXTRA_PARAM_TOKEN)
        )
}
