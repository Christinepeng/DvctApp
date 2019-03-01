package com.divercity.android.features.loadurl

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment


class LoadUrlActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_URL = "intentExtraParamURL"

        fun getCallingIntent(context: Context?, url: String?): Intent {
            val intent = Intent(context, LoadUrlActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_URL, url)
            return intent
        }
    }

    override fun fragment(): BaseFragment = LoadUrlFragment
        .newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_URL))
}
