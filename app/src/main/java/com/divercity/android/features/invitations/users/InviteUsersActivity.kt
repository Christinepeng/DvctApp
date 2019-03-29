package com.divercity.android.features.invitations.users

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class InviteUsersActivity : BaseActivity() {

    companion object {

        public const val PARAM_INVITATION_TYPE = "paramInvitationType"

        // GROUP
        public const val TYPE_GROUP_INVITE = 109
        public const val PARAM_GROUP_ID = "paramGroupId"

        fun getGroupInviteBundle(groupId : String) : Bundle {
            val bundle = Bundle()
            bundle.putInt(
                PARAM_INVITATION_TYPE,
                TYPE_GROUP_INVITE
            )
            bundle.putString(PARAM_GROUP_ID, groupId)
            return bundle
        }

        fun getCallingIntent(
            context: Context?,
            bundle : Bundle
        ): Intent {
            val intent = Intent(context, InviteUsersActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun fragment(): BaseFragment =
        InviteUsersFragment.newInstance(intent.extras)
}