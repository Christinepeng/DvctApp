package com.divercity.android.features.user.addeditworkexperience

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.model.WorkExperience

/**
 * Created by lucas on 05/11/2018.
 */
 
class AddEditWorkExperienceActivity : BaseActivity(){

    companion object {
        private const val INTENT_EXTRA_PARAM_WORK_EXPERIENCE = "extraWorkExperience"

        fun getCallingIntent(context: Context?, workExperience: WorkExperience?): Intent {
            val intent = Intent(context, AddEditWorkExperienceActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_WORK_EXPERIENCE, workExperience)
            return intent
        }
    }

    override fun fragment(): BaseFragment = AddEditWorkExperienceFragment.newInstance(
        intent.getParcelableExtra(INTENT_EXTRA_PARAM_WORK_EXPERIENCE))
}