package com.divercity.android.repository.user

import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.data.entity.device.response.DeviceEntityResponse
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.discard.DiscardConnectionBody
import com.divercity.android.data.entity.workexperience.body.WorkExperienceBody
import com.divercity.android.model.Education
import com.divercity.android.model.WorkExperience
import com.divercity.android.model.user.User
import io.reactivex.Observable

/**
 * Created by lucas on 18/10/2018.
 */

interface UserRepository {

    fun fetchRemoteUserData(userId: String): Observable<User>

    fun updateLoggedUserProfile(user: UserProfileEntity): Observable<User>

    fun fetchLoggedUserData(): Observable<User>

    fun joinGroup(groupId: String): Observable<Unit>

    fun uploadUserPhoto(body: ProfilePictureBody): Observable<User>

    fun followOccupationOfInterests(occupationIds: List<String>): Observable<User>

    fun followInterests(interestsIds: List<String>): Observable<User>

    fun followIndustries(industriesIds: List<String>): Observable<User>

    fun connectUser(userId: String): Observable<ConnectUserResponse>

    fun saveDevice(body: DeviceBody): Observable<DeviceEntityResponse>

    fun updateDevice(deviceId: String, body: DeviceBody): Observable<Boolean>

    fun fetchUnreadMessagesCount(userId: String): Observable<Int>

    fun acceptConnectionRequest(userId: String): Observable<ConnectUserResponse>

    fun declineConnectionRequest(userId: String): Observable<Unit>

    fun markNotificationRead(notificationId: String): Observable<Unit>

    fun fetchFollowersByUser(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun fetchFollowingByUser(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun fetchUsers(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun fetchNotifications(
        pageNumber: Int,
        size: Int
    ): Observable<List<NotificationResponse>>

    fun fetchRecommendedUsers(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun fetchConnectionRequests(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun changePassword(
//        oldPassword: String,
        newPassword: String,
        confirmation: String
    ): Observable<Unit>

    fun fetchEducations(
        userId: String
    ): Observable<List<Education>>

    fun addEducation(
        schoolId: String,
        major: String,
        from: String,
        to: String,
        degreeId: String
    ): Observable<Education>

    fun updateEducation(
        educationId: String,
        schoolId: String?,
        major: String?,
        from: String?,
        to: String?,
        degreeId: String?
    ): Observable<Education>

    fun deleteEducation(
        educationId: String
    ): Observable<Unit>

    //WORK EXPERIENCE

    fun fetchWorkExperiences(userId: String): Observable<List<WorkExperience>>

    fun addExperience(
        experience: WorkExperienceBody
    ): Observable<WorkExperience>

    fun updateExperience(
        experienceId: String,
        experience: WorkExperienceBody
    ): Observable<WorkExperience>

    fun deleteExperience(
        experienceId: String
    ): Observable<Unit>

    fun discardRecommendedConnection(
        body: DiscardConnectionBody
    ): Observable<Unit>
}
