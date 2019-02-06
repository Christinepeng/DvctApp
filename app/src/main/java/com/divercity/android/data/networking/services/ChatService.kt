package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.chat.addchatmemberbody.AddChatMemberBody
import com.divercity.android.data.entity.chat.creategroupchatbody.CreateGroupChatBody
import com.divercity.android.data.entity.chat.currentchats.CurrentChatsResponse
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by lucas on 29/10/2018.
 */

interface ChatService {

    @GET("users/{currentUserId}/chats/{chatId}")
    fun fetchMessages(
        @Path("currentUserId") currentUserId: String,
        @Path("chatId") chatId: String,
        @Query("other_user") otherUserId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataChatMessageResponse>>

    @GET("users/{currentUserId}/chats")
    fun fetchCurrentChats(
        @Path("currentUserId") currentUserId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataObject<CurrentChatsResponse>>>

    @GET("users/{currentUserId}/chats/{chatId}/members")
    fun fetchChatMembers(
        @Path("currentUserId") currentUserId: String,
        @Path("chatId") chatId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>

    @POST("users/{currentUserId}/chats")
    fun createChat(
        @Path("currentUserId") currentUserId: String,
        @Query("other_user") otherUserId: String
    ): Observable<Response<DataObject<CreateChatResponse>>>

    @POST("users/{currentUserId}/chats/{chatId}/add")
    fun addGroupMember(
        @Path("currentUserId") currentUserId: String,
        @Path("chatId") chatId: String,
        @Body addChatMemberBody: AddChatMemberBody
    ): Observable<Response<Void>>

    @POST("users/{currentUserId}/chats")
    fun createGroupChat(
        @Path("currentUserId") currentUserId: String,
        @Query("other_user") otherUserId: String,
        @Body createGroupChatBody: CreateGroupChatBody
    ): Observable<Response<DataObject<CreateChatResponse>>>

    @Multipart
    @POST("messages")
    fun sendMessage(
        @Part("message[content]") message: RequestBody,
        @Part("message[chat_id]") chatId: RequestBody
    ): Observable<ChatMessageResponse>
}