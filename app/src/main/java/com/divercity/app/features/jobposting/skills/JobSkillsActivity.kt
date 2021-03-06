package com.divercity.app.features.jobposting.skills

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.skills.SkillResponse

/**
 * Created by lucas on 17/10/2018.
 */

class JobSkillsActivity  : BaseActivity() {

    companion object {

        const val INTENT_EXTRA_PARAM_SKILLS = "extraParamSkills"

        fun getCallingIntent(context: Context?, skillList : ArrayList<SkillResponse>) : Intent {
            val intent = Intent(context, JobSkillsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_SKILLS, skillList)
            return intent
        }
    }

    override fun fragment(): BaseFragment = JobSkillsFragment.newInstance(
            intent.getParcelableArrayListExtra(INTENT_EXTRA_PARAM_SKILLS))
}
