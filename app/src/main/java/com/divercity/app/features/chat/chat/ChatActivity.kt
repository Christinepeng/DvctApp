package com.divercity.app.features.chat.chat

import android.content.Context
import android.content.Intent
import com.divercity.app.core.base.BaseActivity
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class ChatActivity : BaseActivity() {

    companion object {

        private const val PARAM_USER_ID = "paramUserId"
        private const val PARAM_USER_NAME = "paramUserName"

        fun getCallingIntent(context: Context?, userName: String, userId: String): Intent {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(PARAM_USER_ID, userId)
            intent.putExtra(PARAM_USER_NAME, userName)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ChatFragment
            .newInstance(intent.getStringExtra(PARAM_USER_NAME),
                    intent.getStringExtra(PARAM_USER_ID))
}