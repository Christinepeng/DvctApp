package com.divercity.android.repository.chat

import androidx.paging.DataSource
import com.divercity.android.core.base.BaseRepository
import com.divercity.android.data.entity.chat.addchatmemberbody.AddChatMemberBody
import com.divercity.android.data.entity.chat.creategroupchatbody.CreateGroupChatBody
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.data.networking.services.ChatService
import com.divercity.android.db.dao.ChatMessageDao
import com.divercity.android.db.dao.RecentChatsDao
import com.divercity.android.model.user.User
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class ChatRepositoryImpl @Inject
constructor(
    private val chatService: ChatService,
    private val chatMessageDao: ChatMessageDao,
    private val recentChatsDao: RecentChatsDao
) : BaseRepository(), ChatRepository {

    override fun fetchChatMembers(
        currentUserId: String,
        chatId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return chatService.fetchChatMembers(currentUserId, chatId, pageNumber, size, query)
            .map { response ->
                checkResponse(response)
                response.body()?.data?.map { it.toUser() }
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

    override fun sendMessage(
        message: String,
        chatId: String,
        image: String
    ): Observable<ChatMessageEntityResponse> {
        val partMessage = RequestBody.create(MediaType.parse("text/plain"), message)
        val partChatId = RequestBody.create(MediaType.parse("text/plain"), chatId)
        val partImage = RequestBody.create(MediaType.parse("text/plain"), image)
//        val partAttchmntType = RequestBody.create(MediaType.parse("text/plain"), attchmntType)
//        val partAttchmntId = RequestBody.create(MediaType.parse("text/plain"), attchmntId)

        return chatService.sendMessage(partMessage, partChatId, partImage)
    }

    override fun sendMessageAttachment(
        message: String,
        chatId: String,
        attchmntType: String,
        attchmntId: String
    ): Observable<ChatMessageEntityResponse> {
        val partMessage = RequestBody.create(MediaType.parse("text/plain"), message)
        val partChatId = RequestBody.create(MediaType.parse("text/plain"), chatId)
        val partAttchmntType = RequestBody.create(MediaType.parse("text/plain"), attchmntType)
        val partAttchmntId = RequestBody.create(MediaType.parse("text/plain"), attchmntId)

        return chatService.sendMessageAttachment(
            partMessage,
            partChatId,
            partAttchmntType,
            partAttchmntId
        )
    }

    override suspend fun insertChatMessageOnDB(chatMessageResponse: ChatMessageEntityResponse) {
        withContext(Dispatchers.IO) {
            chatMessageDao.insertChatMessage(chatMessageResponse)
        }
    }

    override suspend fun insertChatMessagesOnDB(list: List<ChatMessageEntityResponse>) {
        withContext(Dispatchers.IO) {
            chatMessageDao.insertChatMessages(list)
        }
    }

    override suspend fun saveRecentChats(list: List<ExistingUsersChatListItem>) {
        recentChatsDao.insertChatMessages(list)
    }

    override suspend fun deleteRecentChatsDB() {
        recentChatsDao.deleteRecentChats()
    }

    override suspend fun deleteChatMessagesDB() {
        chatMessageDao.deleteChatMessages()
    }

    override suspend fun fetchChatIdByUser(userId: String): Int {
        return withContext(Dispatchers.IO) {
            recentChatsDao.fetchChatIdByUser(userId)
        }
    }

    override fun getMessagesByChatId(chatId: Int): DataSource.Factory<Int, ChatMessageEntityResponse> {
        return chatMessageDao.getPagedMessagesByChatId(chatId)
    }

    override fun getRecentChats(): DataSource.Factory<Int, ExistingUsersChatListItem> {
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

    override fun createGroupChat(
        currentUserId: String,
        otherUserId: String,
        createGroupChatBody: CreateGroupChatBody
    ): Observable<CreateChatResponse> {
        return chatService.createGroupChat(currentUserId, otherUserId, createGroupChatBody).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun addGroupMember(
        currentUserId: String,
        chatId: String,
        usersIds: List<String>
    ): Observable<Boolean> {
        return chatService.addGroupMember(currentUserId, chatId, AddChatMemberBody(usersIds)).map {
            checkResponse(it)
            true
        }
    }
}