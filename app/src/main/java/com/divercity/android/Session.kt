package com.divercity.android

import com.divercity.android.repository.chat.ChatRepositoryImpl
import com.divercity.android.repository.user.UserRepository
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
    private val chatRepositoryImpl: ChatRepositoryImpl,
    private val userRepository: UserRepository
) {

    fun logout() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            chatRepositoryImpl.deleteChatDB()
            chatRepositoryImpl.deleteChatMessagesDB()
            withContext(Dispatchers.IO) {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            }
        }
        userRepository.clearUserData()
    }
}