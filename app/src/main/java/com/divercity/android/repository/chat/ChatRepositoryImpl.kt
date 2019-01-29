package com.divercity.android.repository.chat

import android.arch.paging.DataSource
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.services.ChatService
import com.divercity.android.db.dao.ChatMessageDao
import com.divercity.android.db.dao.RecentChatsDao
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class ChatRepositoryImpl @Inject
constructor(
        private val chatService: ChatService,
        private val chatMessageDao: ChatMessageDao,
        private val recentChatsDao: RecentChatsDao
) : ChatRepository {

    override fun fetchChatMembers(currentUserId: String, chatId: String, pageNumber: Int, size: Int, query: String?): Observable<List<UserResponse>> {
        return chatService.fetchChatMembers(currentUserId, chatId, pageNumber, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchMessages(
            currentUserId: String,
            chatId: String,
            otherUserId: String,
            pageNumber: Int,
            size: Int,
            query: String?
    ): Observable<DataChatMessageResponse> {
        return chatService.fetchMessages(
                currentUserId,
                chatId,
                otherUserId,
                pageNumber,
                size,
                query
        ).map {
            checkResponse(it)
            it.body()
        }
    }

    override fun createChat(
            currentUserId: String,
            otherUserId: String
    ): Observable<CreateChatResponse> {
        return chatService.createChat(currentUserId, otherUserId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }

    override fun sendMessage(message: String, chatId: String): Observable<ChatMessageResponse> {
        val partMessage = RequestBody.create(MediaType.parse("text/plain"), message)
        val partChatId = RequestBody.create(MediaType.parse("text/plain"), chatId)

        return chatService.sendMessage(partMessage, partChatId)
    }

    suspend fun insertChatMessageOnDB(chatMessageResponse: ChatMessageResponse) {
        withContext(Dispatchers.IO) {
            chatMessageDao.insertChatMessage(chatMessageResponse)
        }
    }

    suspend fun insertChatMessagesOnDB(list: List<ChatMessageResponse>) {
        withContext(Dispatchers.IO) {
            chatMessageDao.insertChatMessages(list)
        }
    }

    suspend fun countMessagesByChatIdFromDB(chatId: Int): Int {
        return withContext(Dispatchers.IO) {
            chatMessageDao.countMessagesByChatId(chatId)
        }
    }

    suspend fun saveRecentChats(list: List<ExistingUsersChatListItem>) {
        return withContext(Dispatchers.IO) {
            recentChatsDao.insertChatMessages(list)
        }
    }

    suspend fun insertChatOnDB(chat: ExistingUsersChatListItem) {
        return withContext(Dispatchers.IO) {
            recentChatsDao.insertChat(chat)
        }
    }

    suspend fun deleteChatMessagesDB() {
        return withContext(Dispatchers.IO) {
            chatMessageDao.deleteChatMessages()
        }
    }

    suspend fun deleteChatDB() {
        return withContext(Dispatchers.IO) {
            //            chatMessageDao.deleteChat()
        }
    }

    suspend fun fetchChatIdByUser(userId: String): Int {
        return withContext(Dispatchers.IO) {
            recentChatsDao.fetchChatIdByUser(userId)
        }
    }

    fun getMessagesByChatId(chatId: Int): DataSource.Factory<Int, ChatMessageResponse> {
        return chatMessageDao.getPagedMessagesByChatId(chatId)
    }

    fun getRecentChats(): DataSource.Factory<Int, ExistingUsersChatListItem> {
        return recentChatsDao.getPagedRecentChats()
    }

    override fun fetchCurrentChats(
            currentUserId: String,
            pageNumber: Int,
            size: Int,
            query: String?
    ): Observable<List<ExistingUsersChatListItem>> {
        return chatService.fetchCurrentChats(currentUserId, pageNumber, size, query).map {
            checkResponse(it)
            it.body()?.data?.existingUsersChatList
        }
    }
}