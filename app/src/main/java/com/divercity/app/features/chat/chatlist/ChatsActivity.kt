package com.divercity.app.features.chat.chatlist

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class ChatsActivity : BaseActivity() {

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, ChatsActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment =
            ChatsFragment.newInstance()
}