package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.data.entity.activity.notificationread.NotificationReadBody
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.data.entity.device.response.DeviceResponse
import com.divercity.android.data.entity.industry.body.FollowIndustryBody
import com.divercity.android.data.entity.interests.body.FollowInterestsBody
import com.divercity.android.data.entity.occupationofinterests.body.FollowOOIBody
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody
import com.divercity.android.data.entity.profile.profile.UserProfileBody
import com.divercity.android.data.entity.unreadmessagecount.UnreadMessageCountResponse
import com.divercity.android.data.entity.user.connectuser.body.UserConnectionBody
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by lucas on 17/10/2018.
 */

interface UserService {

    @GET("users/{id}")
    fun fetchUserData(@Path("id") userId: String): Observable<Response<DataObject<UserResponse>>>

    @PUT("users/{id}")
    fun updateUserProfile(@Path("id") userId: String, @Body body: UserProfileBody): Observable<Response<DataObject<UserResponse>>>

    @POST("group_of_interests/{id}/follow")
    fun joinGroup(@Path("id") idGroup: String): Observable<Response<Void>>

    @POST("users/avatar_upload")
    fun uploadUserPhoto(@Body body: ProfilePictureBody): Observable<Response<DataObject<UserResponse>>>

    @POST("data/follow_occupation_of_interest")
    fun followOccupationOfInterests(@Body body: FollowOOIBody): Observable<Response<DataObject<UserResponse>>>

    @POST("users/current/update_interests")
    fun followInterests(@Body body: FollowInterestsBody): Observable<Response<DataObject<UserResponse>>>

    @GET("users/{userId}/chats/unread_messages_count")
    fun fetchUnreadMessagesCount(@Path("userId") userId: String): Observable<Response<DataObject<UnreadMessageCountResponse>>>

    @POST("devices")
    fun saveDevice(@Body body: DeviceBody): Observable<Response<DataObject<DeviceResponse>>>

    @PUT("devices/{deviceId}")
    fun updateDevice(@Path("deviceId") deviceId: String, @Body body: DeviceBody): Observable<Response<Void>>

    @Multipart
    @POST("users/connect")
    fun connectUser(@Part("user_id") userId: RequestBody): Observable<Response<DataObject<ConnectUserResponse>>>

    @POST("users/accept_connection")
    fun acceptConnectionRequest(@Body body: UserConnectionBody): Observable<Response<DataObject<ConnectUserResponse>>>

    @HTTP(method = "DELETE", path = "users/remove_connection", hasBody = true)
    fun cancelConnectionRequest(@Body body: UserConnectionBody): Observable<Response<Unit>>

    @POST("data/follow_industry")
    fun followIndustries(@Body body: FollowIndustryBody): Observable<Response<DataObject<UserResponse>>>

    @GET("users/{id}/followers")
    fun fetchFollowersByUser(
        @Path("id") userId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>

    @GET("users/{id}/following")
    fun fetchFollowingByUser(
        @Path("id") userId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>

    @GET("users?order_by_name=asc")
    fun fetchUsers(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>

    @GET("activity_records")
    fun fetchNotifications(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int
    ): Observable<Response<DataArray<NotificationResponse>>>

    @PUT("activity_records/mark_read")
    fun markNotificationRead(@Body body: NotificationReadBody): Observable<Response<Unit>>

    @GET("recommenders/users")
    fun fetchRecommendedUsers(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>

    @GET("users/{userId}/connection_requests")
    fun fetchConnectionRequests(
        @Path("userId") userId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<UserResponse>>>
}
