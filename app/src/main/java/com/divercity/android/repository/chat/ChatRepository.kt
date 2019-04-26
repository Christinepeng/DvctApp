package com.divercity.android.repository.chat

import androidx.paging.DataSource
import com.divercity.android.data.entity.chat.creategroupchatbody.CreateGroupChatBody
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.model.user.User
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface ChatRepository {

    fun createChat(currentUserId: String, otherUserId: String): Observable<CreateChatResponse>

    fun createGroupChat(
        currentUserId: String,
        otherUserId: String,
        createGroupChatBody: CreateGroupChatBody
    ): Observable<CreateChatResponse>

    fun addGroupMember(
        currentUserId: String,
        chatId: String,
        usersIds: List<String>
    ): Observable<Boolean>

    fun fetchMessages(
        currentUserId: String,
        chatId: String,
        otherUserId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<DataChatMessageResponse>

    fun fetchCurrentChats(
        currentUserId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<ExistingUsersChatListItem>>

    fun sendMessage(
        message: String,
        chatId: String,
        image: String
    ): Observable<ChatMessageEntityResponse>

    fun sendMessageAttachment(
        message: String,
        chatId: String,
        attchmntType: String,
        attchmntId: String
    ): Observable<ChatMessageEntityResponse>

    fun fetchChatMembers(
        currentUserId: String,
        chatId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun getMessagesByChatId(chatId: Int): DataSource.Factory<Int, ChatMessageEntityResponse>

    fun getRecentChats(): DataSource.Factory<Int, ExistingUsersChatListItem>

    suspend fun fetchChatIdByUser(userId: String): Int

    suspend fun deleteChatMessagesDB()

    suspend fun deleteRecentChatsDB()

    suspend fun saveRecentChats(list: List<ExistingUsersChatListItem>)

    suspend fun insertChatMessagesOnDB(list: List<ChatMessageEntityResponse>)

    suspend fun insertChatMessageOnDB(chatMessageResponse: ChatMessageEntityResponse)
}