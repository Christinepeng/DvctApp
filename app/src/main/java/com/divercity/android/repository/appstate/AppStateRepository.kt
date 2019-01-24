package com.divercity.android.repository.appstate

/**
 * Created by lucas on 26/10/2018.
 */

interface AppStateRepository {

    fun setChatUserId(chatId: Int)

    fun getChatUserId(): Int
}