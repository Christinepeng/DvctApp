package com.divercity.android.features.user.addediteducation

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.model.Education

/**
 * Created by lucas on 05/11/2018.
 */

class AddEditEducationActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_EDUCATION = "extraEducation"

        fun getCallingIntent(context: Context?, education: Education?): Intent {
            val intent = Intent(context, AddEditEducationActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_EDUCATION, education)
            return intent
        }
    }

    override fun fragment(): BaseFragment = AddEditEducationFragment.newInstance(
        intent.getParcelableExtra(INTENT_EXTRA_PARAM_EDUCATION)
    )
}