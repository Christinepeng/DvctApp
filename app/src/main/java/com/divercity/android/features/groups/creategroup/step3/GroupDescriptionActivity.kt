package com.divercity.android.features.groups.creategroup.step3

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class GroupDescriptionActivity : BaseActivity() {

    companion object {

        private const val INTENT_EXTRA_GROUP_NAME = "extraGroupName"
        private const val INTENT_EXTRA_GROUP_PHOTO = "extraGroupPhoto"

        fun getCallingIntent(context: Context?, groupName : String, photoPath : String?): Intent {
            val intent = Intent(context, GroupDescriptionActivity::class.java)
            intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName)
            intent.putExtra(INTENT_EXTRA_GROUP_PHOTO, photoPath)
            return intent
        }
    }

    override fun fragment(): BaseFragment = GroupDescriptionFragment.newInstance(
        intent.getStringExtra(INTENT_EXTRA_GROUP_NAME),
        intent.getStringExtra(INTENT_EXTRA_GROUP_PHOTO)
    )
}
