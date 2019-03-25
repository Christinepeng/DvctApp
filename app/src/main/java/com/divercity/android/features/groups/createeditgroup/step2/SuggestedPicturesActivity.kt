package com.divercity.android.features.groups.createeditgroup.step2

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SuggestedPicturesActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return  Intent(context, SuggestedPicturesActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = SuggestedPicturesFragment.newInstance()
}