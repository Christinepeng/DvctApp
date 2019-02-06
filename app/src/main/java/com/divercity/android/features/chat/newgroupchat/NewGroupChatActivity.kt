package com.divercity.android.features.chat.newgroupchat

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class NewGroupChatActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, NewGroupChatActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
            NewGroupChatFragment.newInstance()

}