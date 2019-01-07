package com.divercity.app.repository.chat

import com.divercity.app.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.app.data.entity.createchat.CreateChatResponse
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
}