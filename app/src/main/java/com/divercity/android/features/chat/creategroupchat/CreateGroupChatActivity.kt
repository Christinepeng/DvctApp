package com.divercity.android.features.chat.creategroupchat

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class CreateGroupChatActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, CreateGroupChatActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
            CreateGroupChatFragment.newInstance()

}