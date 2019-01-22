package com.divercity.android.repository.chat

import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface ChatRepository {

    fun createChat(currentUserId: String, otherUserId: String): Observable<CreateChatResponse>

    fun fetchMessages(currentUserId: String,
                      chatId: String,
                      otherUserId: String,
                      pageNumber: Int,
                      size: Int,
                      query: String?): Observable<DataChatMessageResponse>

    fun fetchCurrentChats(currentUserId: String,
                      pageNumber: Int,
                      size: Int,
                      query: String?): Observable<List<ExistingUsersChatListItem>>

    fun sendMessage(message: String,
                    chatId: String): Observable<ChatMessageResponse>

    fun fetchChatMembers(currentUserId: String,
                         chatId: String,
                         pageNumber: Int,
                         size: Int,
                         query: String?): Observable<List<UserResponse>>
}