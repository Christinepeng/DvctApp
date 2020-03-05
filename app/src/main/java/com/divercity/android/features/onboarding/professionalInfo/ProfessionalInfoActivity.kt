package com.divercity.android.features.onboarding.professionalInfo

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 25/10/2018.
 */

class ProfessionalInfoActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context) : Intent {
            return Intent(context, ProfessionalInfoActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = ProfessionalInfoFragment.newInstance()
}
