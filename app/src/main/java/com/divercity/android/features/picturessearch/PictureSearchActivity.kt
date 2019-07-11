package com.divercity.android.features.picturessearch

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class PictureSearchActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return  Intent(context, PictureSearchActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = PictureSearchFragment.newInstance()
}
