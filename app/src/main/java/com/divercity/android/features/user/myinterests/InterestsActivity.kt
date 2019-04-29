package com.divercity.android.features.user.myinterests

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class InterestsActivity : BaseActivity() {

    companion object {

        private const val INTENT_EXTRA_PARAM_IS_EDITION = "isEdition"

        fun getCallingIntent(context: Context?, isEdition: Boolean): Intent {
            val intent = Intent(context, InterestsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_IS_EDITION, isEdition)
            return intent
        }
    }

    override fun fragment(): BaseFragment = InterestsFragment.newInstance(
        intent.getBooleanExtra(INTENT_EXTRA_PARAM_IS_EDITION, false)
    )
}
