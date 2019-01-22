package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.data.entity.group.creategroup.CreateGroupBody
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by lucas on 29/10/2018.
 */

interface GroupService {

    @GET("group_of_interests/onboarding")
    fun fetchGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests/trending")
    fun fetchTrendingGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests")
    fun fetchAllGroups(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests/my_groups")
    fun fetchMyGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests/{groupId}")
    fun fetchGroupById(@Path("groupId") groupId: String): Observable<Response<DataObject<GroupResponse>>>

    @GET("group_of_interests/{groupId}/members")
    fun fetchGroupMembers(@Path("groupId") groupId: String,
                          @Query("page[number]") pageNumber: Int,
                          @Query("page[size]") size: Int,
                          @Query("search_query") query: String?): Observable<Response<DataArray<UserResponse>>>

    @GET("group_of_interests/{groupId}/admins")
    fun fetchGroupAdmins(@Path("groupId") groupId: String,
                         @Query("page[number]") pageNumber: Int,
                         @Query("page[size]") size: Int,
                         @Query("search_query") query: String?): Observable<Response<DataArray<UserResponse>>>

    @POST("group_of_interests/{groupId}/join")
    fun requestToJoinGroup(@Path("groupId") groupId: String): Observable<MessageResponse>

    @POST("group_of_interests")
    fun createGroup(@Body body: CreateGroupBody): Observable<Response<DataObject<GroupResponse>>>

    @GET("questions")
    fun fetchQuestions(@Query("group_id") groupId: String,
                       @Query("page[number]") pageNumber: Int,
                       @Query("page[size]") size: Int,
                       @Query("search_query") query: String?): Observable<Response<DataArray<QuestionResponse>>>

    @GET("group_of_interests/following")
    fun fetchFollowedGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?): Observable<Response<DataArray<GroupResponse>>>

    @Multipart
    @PUT("api/group_of_interests")
    fun createGroup(@Part("group_of_interest[title]") title: RequestBody,
                    @Part("group_of_interest[description]") description: RequestBody,
                    @Part("group_of_interest[group_type]") groupType: RequestBody,
                    @Part("group_of_interest[picture]") picture: RequestBody): Observable<Response<DataObject<GroupResponse>>>

    @GET("recommenders/group_of_interests")
    fun fetchRecommendedGroups(@Query("page[number]") pageNumber: Int,
                               @Query("page[size]") size: Int): Observable<Response<DataArray<GroupResponse>>>
}