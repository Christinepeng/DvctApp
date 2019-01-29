package com.divercity.android

import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by lucas on 14/01/2019.
 */

class Session @Inject
constructor(
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository
) {

    fun logout() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            chatRepository.deleteRecentChatsDB()
            chatRepository.deleteChatMessagesDB()
            withContext(Dispatchers.IO) {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            }
        }
        sessionRepository.clearUserData()
    }
}