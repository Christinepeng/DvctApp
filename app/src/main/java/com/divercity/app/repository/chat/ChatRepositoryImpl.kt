package com.divercity.app.repository.chat

import com.divercity.app.data.entity.createchat.CreateChatResponse
import com.divercity.app.data.networking.services.ChatService
import com.divercity.app.db.ChatMessageDao
import io.reactivex.Observable
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class ChatRepositoryImpl @Inject
constructor(private val chatService: ChatService,
            private val chatMessageDao: ChatMessageDao) : ChatRepository {

    override fun createChat(currentUserId: String, otherUserId: String): Observable<CreateChatResponse> {
        return chatService.createChat(currentUserId, otherUserId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }
}