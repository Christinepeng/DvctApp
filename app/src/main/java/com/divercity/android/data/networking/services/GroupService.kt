package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.group.answer.body.AnswerBody
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import com.divercity.android.data.entity.group.creategroup.CreateGroupBody
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.group.invitation.GroupInviteResponse
import com.divercity.android.data.entity.group.invitation.contact.GroupInviteContactBody
import com.divercity.android.data.entity.group.invitation.user.GroupInviteUserBody
import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse
import com.divercity.android.data.entity.group.question.NewQuestionBody
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
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
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests/trending")
    fun fetchTrendingGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests")
    fun fetchAllGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests/my_groups")
    fun fetchMyGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<GroupResponse>>>

    @GET("group_of_interests/{groupId}")
    fun fetchGroupById(@Path("groupId") groupId: String): Observable<Response<DataObject<GroupResponse>>>

    @GET("group_of_interests/{groupId}/members")
    fun fetchGroupMembers(
            @Path("groupId") groupId: String,
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>

    @GET("group_of_interests/{groupId}/admins")
    fun fetchGroupAdmins(
            @Path("groupId") groupId: String,
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>

    @POST("group_of_interests/{groupId}/join")
    fun requestToJoinGroup(@Path("groupId") groupId: String): Observable<MessageResponse>

    @POST("group_of_interests")
    fun createGroup(@Body body: CreateGroupBody): Observable<Response<DataObject<GroupResponse>>>

    @DELETE("group_of_interests/{groupId}")
    fun deleteGroup(
            @Path("groupId") groupId: String
    ): Observable<Response<Void>>

    @PUT("group_of_interests/{groupId}")
    fun editGroup(
            @Path("groupId") groupId: String,
            @Body body: CreateGroupBody
    ): Observable<Response<DataObject<GroupResponse>>>

    @GET("questions")
    fun fetchQuestions(
            @Query("group_id") groupId: String,
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<QuestionResponse>>>

    @GET("group_of_interests/following")
    fun fetchFollowedGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<GroupResponse>>>

    @GET("recommenders/group_of_interests")
    fun fetchRecommendedGroups(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int
    ): Observable<Response<DataArray<GroupResponse>>>

    @POST("group_of_interests/group_invite")
    fun inviteContact(
            @Body body: GroupInviteContactBody
    ): Observable<GroupInviteResponse>

    @POST("group_of_interests/group_invite")
    fun inviteUsers(
            @Body body: GroupInviteUserBody
    ): Observable<GroupInviteResponse>

    @GET("group_of_interests/my_invites")
    fun fetchGroupInvitations(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int
    ): Observable<Response<DataArray<GroupInvitationNotificationResponse>>>

    @GET("group_of_interests/requests")
    fun fetchGroupJoinRequests(
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int
    ): Observable<Response<DataArray<JoinGroupRequestResponse>>>

    @POST("questions")
    fun createNewTopic(
            @Body body: NewQuestionBody
    ): Observable<Response<DataObject<QuestionResponse>>>

    @GET("questions/{questionId}/answers")
    fun fetchAnswers(
            @Path("questionId") questionId: String,
            @Query("page[number]") pageNumber: Int,
            @Query("page[size]") size: Int,
            @Query("search_query") query: String?
    ): Observable<Response<DataArray<AnswerResponse>>>

    @POST("answers")
    fun sendNewAnswer(
            @Body body: AnswerBody
    ): Observable<Response<DataObject<AnswerResponse>>>

    @Multipart
    @POST("group_of_interests/accept_invite")
    fun acceptGroupInvite(
        @Part("invite_id") inviteIdPart: RequestBody
    ): Observable<Response<Unit>>

    @Multipart
    @POST("group_of_interests/decline_invite")
    fun declineGroupInvite(
        @Part("invite_id") inviteIdPart: RequestBody
    ): Observable<Response<Unit>>

    @POST("group_of_interests/{groupId}/accept/{userId}")
    fun acceptJoinGroupRequest(
        @Path("groupId") groupId : String,
        @Path("userId") userId : String
    ): Observable<Response<Unit>>

    @POST("group_of_interests/{groupId}/decline/{userId}")
    fun declineJoinGroupRequest(
        @Path("groupId") groupId : String,
        @Path("userId") userId : String
    ): Observable<Response<Unit>>
}