package com.divercity.android.features.groups.createtopic

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.group.GroupResponse

class CreateTopicActivity : BaseActivity() {

    companion object {

        private const val INTENT_EXTRA_PARAM_GROUP = "group"

        fun getCallingIntent(context: Context?, group: GroupResponse?): Intent {
            val intent = Intent(context, CreateTopicActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP, group)
            return intent
        }
    }

    override fun fragment(): BaseFragment = CreateTopicFragment.newInstance(
        intent.getParcelableExtra(INTENT_EXTRA_PARAM_GROUP))
}
