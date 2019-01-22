package com.divercity.android

import com.divercity.android.repository.chat.ChatRepositoryImpl
import com.divercity.android.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            chatRepositoryImpl.deleteChatDB()
            chatRepositoryImpl.deleteChatMessagesDB()
        }
        userRepository.clearUserData()
    }
}