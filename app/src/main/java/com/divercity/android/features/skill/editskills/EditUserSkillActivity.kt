package com.divercity.android.features.skill.editskills

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class EditUserSkillActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_PREV_SKILL = "intentExtraParamPrevSkill"

        fun getCallingIntent(context: Context?, prevSkills : ArrayList<String>?) : Intent {
            val intent = Intent(context, EditUserSkillActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_PREV_SKILL,prevSkills)
            return intent
        }
    }

    override fun fragment(): BaseFragment = EditUserSkillFragment
        .newInstance(intent.getStringArrayListExtra(INTENT_EXTRA_PARAM_PREV_SKILL))
}
