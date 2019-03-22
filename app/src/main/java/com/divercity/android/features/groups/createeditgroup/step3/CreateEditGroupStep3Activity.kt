package com.divercity.android.features.groups.createeditgroup.step3

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.group.group.GroupResponse

class CreateEditGroupStep3Activity : BaseActivity() {

    companion object {

        private const val INTENT_EXTRA_GROUP_NAME = "extraGroupName"
        private const val INTENT_EXTRA_GROUP_PHOTO = "extraGroupPhoto"
        private const val INTENT_EXTRA_PARAM_GROUP = "extraGroup"

        fun getCallingIntent(
            context: Context?,
            groupName: String?,
            photoPath: String?,
            group: GroupResponse?
        ): Intent {
            val intent = Intent(context, CreateEditGroupStep3Activity::class.java)
            intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName)
            intent.putExtra(INTENT_EXTRA_GROUP_PHOTO, photoPath)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP, group)

            return intent
        }
    }

    override fun fragment(): BaseFragment = CreateEditGroupStep3Fragment.newInstance(
        intent.getStringExtra(INTENT_EXTRA_GROUP_NAME),
        intent.getStringExtra(INTENT_EXTRA_GROUP_PHOTO),
        intent.getParcelableExtra(INTENT_EXTRA_PARAM_GROUP)
    )
}
