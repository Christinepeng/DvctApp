package com.divercity.android.repository.chat

import androidx.paging.DataSource
import com.divercity.android.data.entity.chat.creategroupchatbody.CreateGroupChatBody
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
    ): Observable<ChatMessageResponse>

//    fun sendMessageAttachment(
//        message: String,
//        chatId: String,
//        attchmntType: String,
//        attchmntId: String
//    ): Observable<ChatMessageResponse>

    fun fetchChatMembers(
        currentUserId: String,
        chatId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>

    fun getMessagesByChatId(chatId: Int): DataSource.Factory<Int, ChatMessageResponse>

    fun getRecentChats(): DataSource.Factory<Int, ExistingUsersChatListItem>

    suspend fun fetchChatIdByUser(userId: String): Int

    suspend fun deleteChatMessagesDB()

    suspend fun deleteRecentChatsDB()

    suspend fun saveRecentChats(list: List<ExistingUsersChatListItem>)

    suspend fun insertChatMessagesOnDB(list: List<ChatMessageResponse>)

    suspend fun insertChatMessageOnDB(chatMessageResponse: ChatMessageResponse)
}