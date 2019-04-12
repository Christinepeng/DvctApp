package com.divercity.android.repository.user

import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.data.entity.device.response.DeviceResponse
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody
import com.divercity.android.data.entity.profile.profile.User
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.entity.workexperience.body.WorkExperienceBody
import com.divercity.android.data.entity.workexperience.response.WorkExperienceResponse
import io.reactivex.Observable

/**
 * Created by lucas on 18/10/2018.
 */

interface UserRepository {

    fun fetchRemoteUserData(userId: String): Observable<UserResponse>

    fun updateLoggedUserProfile(user: User): Observable<UserResponse>

    fun joinGroup(groupId: String): Observable<Boolean>

    fun uploadUserPhoto(body: ProfilePictureBody): Observable<UserResponse>

    fun followOccupationOfInterests(occupationIds: List<String>): Observable<UserResponse>

    fun followInterests(interestsIds: List<String>): Observable<UserResponse>

    fun followIndustries(industriesIds: List<String>): Observable<UserResponse>

    fun connectUser(userId: String): Observable<ConnectUserResponse>

    fun saveDevice(body: DeviceBody): Observable<DeviceResponse>

    fun updateDevice(deviceId: String, body: DeviceBody): Observable<Boolean>

    fun fetchUnreadMessagesCount(userId: String): Observable<Int>

    fun acceptConnectionRequest(userId: String): Observable<ConnectUserResponse>

    fun declineConnectionRequest(userId: String): Observable<Unit>

    fun markNotificationRead(notificationId: String): Observable<Unit>

    fun fetchWorkExperiences(userId: String): Observable<List<WorkExperienceResponse>>

    fun addNewExperience(
        userId: String,
        experience : WorkExperienceBody
    ): Observable<WorkExperienceResponse>

    fun fetchFollowersByUser(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>

    fun fetchFollowingByUser(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>

    fun fetchUsers(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>

    fun fetchNotifications(
        pageNumber: Int,
        size: Int
    ): Observable<List<NotificationResponse>>

    fun fetchRecommendedUsers(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>

    fun fetchConnectionRequests(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>
}
