package com.divercity.app.data.networking.services

import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.chat.currentchats.CurrentChatsResponse
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.app.data.entity.createchat.CreateChatResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by lucas on 29/10/2018.
 */

interface ChatService {

    @GET("users/{currentUserId}/chats/{chatId}")
    fun fetchMessages(@Path("currentUserId") currentUserId: String,
                      @Path("chatId") chatId: String,
                      @Query("other_user") otherUserId: String,
                      @Query("page[number]") pageNumber: Int,
                      @Query("page[size]") size: Int,
                      @Query("search_query") query: String?): Observable<Response<DataChatMessageResponse>>

    @GET("users/{currentUserId}/chats")
    fun fetchCurrentChats(@Path("currentUserId") currentUserId: String,
                      @Query("page[number]") pageNumber: Int,
                      @Query("page[size]") size: Int,
                      @Query("search_query") query: String?): Observable<Response<DataObject<CurrentChatsResponse>>>

    @POST("users/{currentUserId}/chats")
    fun createChat(@Path("currentUserId") currentUserId: String,
                   @Query("other_user") otherUserId: String): Observable<Response<DataObject<CreateChatResponse>>>

    @Multipart
    @POST("messages")
    fun sendMessage(@Part("message[content]") message: RequestBody,
                    @Part("message[chat_id]") chatId: RequestBody): Observable<ChatMessageResponse>
}