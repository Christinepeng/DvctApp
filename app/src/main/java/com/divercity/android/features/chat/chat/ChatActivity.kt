package com.divercity.android.features.chat.chat

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/12/2018.
 */

class ChatActivity : BaseActivity() {

    companion object {

        private const val PARAM_USER_ID = "paramUserId"
        private const val PARAM_DISPLAY_NAME = "paramDisplayName"
        private const val PARAM_CHAT_ID = "paramChatId"

        fun getCallingIntent(
            context: Context?,
            userName: String,
            userId: String?,
            chatId: Int?
        ): Intent {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(PARAM_USER_ID, userId)
            intent.putExtra(PARAM_DISPLAY_NAME, userName)
            intent.putExtra(PARAM_CHAT_ID, chatId)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ChatFragment
        .newInstance(
            intent.getStringExtra(PARAM_DISPLAY_NAME),
            intent.getStringExtra(PARAM_USER_ID),
            intent.getIntExtra(PARAM_CHAT_ID, -1)
        )

    override fun onBackPressed() {
        if (isTaskRoot) {
            navigator.navigateToChatsActivity(this)
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}