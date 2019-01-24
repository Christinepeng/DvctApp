package com.divercity.android.repository.appstate

import android.content.Context
import com.divercity.android.core.sharedpref.SharedPreferencesManager

/**
 * Created by lucas on 24/10/2018.
 */

class AppStateRepositoryImpl(val context: Context) : AppStateRepository {

    companion object {
        const val STATE_PREF_NAME = "APP_STATE"

    }
    enum class Key {
        ACTIVE_USER_CHAT_ID,
    }

    private val sharedPreferencesManager: SharedPreferencesManager<Key> =
            SharedPreferencesManager(context, STATE_PREF_NAME)

    override fun setChatUserId(chatId: Int) {
        sharedPreferencesManager.put(Key.ACTIVE_USER_CHAT_ID, chatId)
    }

    override fun getChatUserId(): Int {
        return sharedPreferencesManager.getInt(Key.ACTIVE_USER_CHAT_ID)
    }
}