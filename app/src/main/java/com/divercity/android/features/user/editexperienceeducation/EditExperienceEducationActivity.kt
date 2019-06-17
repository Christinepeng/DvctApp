package com.divercity.android.features.user.editexperienceeducation

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class EditExperienceEducationActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, EditExperienceEducationActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = EditExperienceEducationFragment.newInstance()
}
