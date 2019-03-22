package com.divercity.android.features.groups.createeditgroup.step1

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.group.group.GroupResponse

class CreateEditGroupStep1Activity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_GROUP = "extraGroup"

        fun getCallingIntent(context: Context?, group: GroupResponse?): Intent {
            val intent = Intent(context, CreateEditGroupStep1Activity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP, group)
            return intent
        }
    }

    override fun fragment(): BaseFragment = CreateEditGroupStep1Fragment.newInstance(
        intent.getParcelableExtra(INTENT_EXTRA_PARAM_GROUP))
}
