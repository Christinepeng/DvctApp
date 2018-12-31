package com.divercity.app.data.networking.services

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.createchat.CreateChatResponse
import com.divercity.app.data.entity.job.response.JobResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by lucas on 29/10/2018.
 */

interface ChatService {

    @GET("users/{currentUserId}/chats/{chatId}")
    fun fetchMessages(@Path("currentUserId") currentUserId : String,
                  @Path("chatId") chatId : String,
                  @Query("other_user") otherUserId: String,
                  @Query("page[number]") pageNumber: Int,
                  @Query("page[size]") size: Int,
                  @Query("search_query") query: String?): Observable<Response<DataArray<JobResponse>>>

    @POST("users/{currentUserId}/chats")
    fun createChat(@Path("currentUserId") currentUserId : String,
                   @Query("other_user") otherUserId: String): Observable<Response<DataObject<CreateChatResponse>>>
}