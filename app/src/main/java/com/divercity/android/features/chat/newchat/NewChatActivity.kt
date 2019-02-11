package com.divercity.android.features.chat.newchat

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class NewChatActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, NewChatActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
            NewChatFragment.newInstance()

}