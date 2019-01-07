package com.divercity.app.repository.chat

import android.arch.paging.DataSource
import com.divercity.app.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.app.data.entity.createchat.CreateChatResponse
import com.divercity.app.data.networking.services.ChatService
import com.divercity.app.db.chat.Chat
import com.divercity.app.db.chat.ChatMessageDao
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
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {

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

    suspend fun insertChatOnDB(chat: Chat) {
        return withContext(Dispatchers.IO) {
            chatMessageDao.insertChat(chat)
        }
    }

    suspend fun getChatIdByOtherUserIdFromDB(otherUserId: Int): Int {
        return withContext(Dispatchers.IO) {
            chatMessageDao.getChatIdByOtherUserId(otherUserId)
        }
    }

    fun getMessagesByChatId(chatId: Int): DataSource.Factory<Int, ChatMessageResponse> {
        return chatMessageDao.getPagedMessagesByChatId(chatId)
    }

    override fun fetchCurrentChats(
        currentUserId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<ExistingUsersChatListItem>> {
        return chatService.fetchCurrentChats(currentUserId, pageNumber, size,query).map {
            checkResponse(it)
            it.body()?.data?.existingUsersChatList
        }
    }
}