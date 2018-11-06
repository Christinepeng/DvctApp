package com.divercity.app.features.location

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

class SelectLocationActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_CALLED_FROM = "calledFrom"

        fun getCallingIntent(context: Context, calledFrom : String) : Intent {
            val intent = Intent(context, SelectLocationActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_CALLED_FROM,calledFrom)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ToolbarLocationFragment.newInstance(
            intent.getStringExtra(INTENT_EXTRA_PARAM_CALLED_FROM))
}
