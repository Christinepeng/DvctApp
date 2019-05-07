package com.divercity.android.repository.user

import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.data.entity.activity.notificationread.DataItem
import com.divercity.android.data.entity.activity.notificationread.NotificationReadBody
import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.data.entity.device.response.DeviceEntityResponse
import com.divercity.android.data.entity.industry.body.FollowIndustryBody
import com.divercity.android.data.entity.interests.body.FollowInterestsBody
import com.divercity.android.data.entity.occupationofinterests.body.FollowOOIBody
import com.divercity.android.data.entity.password.ChangePasswordEntityBody
import com.divercity.android.data.entity.password.PasswordEntity
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.data.entity.profile.profile.UserProfileEntityBody
import com.divercity.android.data.entity.user.connectuser.body.UserConnectionBody
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.workexperience.body.WorkExperienceBody
import com.divercity.android.data.entity.workexperience.response.WorkExperienceResponse
import com.divercity.android.data.networking.services.UserService
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by lucas on 18/10/2018.
 */

class UserRepositoryImpl @Inject
constructor(
    private val userService: UserService,
    private val sessionRepository: SessionRepository
) : UserRepository {

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }

    override fun fetchRemoteUserData(userId: String): Observable<User> {
        return userService.fetchUserData(userId)
            .map { response ->
                checkResponse(response)
                response.body()!!.data.toUser()
            }
    }

    override fun fetchLoggedUserData(): Observable<User> {
        return userService.fetchUserData(sessionRepository.getUserId())
            .map { response ->
                checkResponse(response)
                sessionRepository.saveUserData(response.body()!!.data)
                response.body()!!.data.toUser()
            }
    }

    override fun joinGroup(groupId: String): Observable<Boolean> {
        return userService.joinGroup(groupId).map { response ->
            checkResponse(response)
            true
        }
    }

    override fun uploadUserPhoto(body: ProfilePictureBody): Observable<User> {
        return userService.uploadUserPhoto(body).map { response ->
            checkResponse(response)
            sessionRepository.saveUserData(response.body()!!.data)
            response.body()!!.data.toUser()
        }
    }

    override fun updateLoggedUserProfile(user: UserProfileEntity): Observable<User> {
        val userProfileBody =
            UserProfileEntityBody()
        userProfileBody.user = user
        return userService.updateUserProfile(sessionRepository.getUserId(), userProfileBody)
            .map { response ->
                checkResponse(response)
                sessionRepository.saveUserData(response.body()!!.data)
                response.body()!!.data.toUser()
            }
    }

    override fun followOccupationOfInterests(occupationIds: List<String>): Observable<User> {
        return userService.followOccupationOfInterests(FollowOOIBody(occupationIds))
            .map { response ->
                checkResponse(response)
                response.body()!!.data.toUser()
            }
    }

    override fun followInterests(interestsIds: List<String>): Observable<User> {
        return userService.followInterests(FollowInterestsBody(interestsIds)).map { response ->
            checkResponse(response)
            sessionRepository.saveUserData(response.body()!!.data)
            response.body()!!.data.toUser()
        }
    }

    override fun followIndustries(industriesIds: List<String>): Observable<User> {
        return userService.followIndustries(FollowIndustryBody(industriesIds)).map { response ->
            checkResponse(response)
            sessionRepository.saveUserData(response.body()!!.data)
            response.body()!!.data.toUser()
        }
    }

    override fun fetchFollowersByUser(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return userService.fetchFollowersByUser(userId, pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data.map { it.toUser() }
        }
    }

    override fun fetchFollowingByUser(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return userService.fetchFollowingByUser(userId, pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data.map { it.toUser() }
        }
    }

    override fun fetchUsers(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return userService.fetchUsers(pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data.map { it.toUser() }
        }
    }

    override fun connectUser(userId: String): Observable<ConnectUserResponse> {
        val partUserId = RequestBody.create(MediaType.parse("text/plain"), userId)
        return userService.connectUser(partUserId).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun saveDevice(body: DeviceBody): Observable<DeviceEntityResponse> {
        return userService.saveDevice(body).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun updateDevice(deviceId: String, body: DeviceBody): Observable<Boolean> {
        return userService.updateDevice(deviceId, body).map { response ->
            checkResponse(response)
            true
        }
    }

    override fun fetchUnreadMessagesCount(userId: String): Observable<Int> {
        return userService.fetchUnreadMessagesCount(userId).map { response ->
            checkResponse(response)
            response.body()!!.data.count
        }
    }

    override fun fetchNotifications(
        pageNumber: Int,
        size: Int
    ): Observable<List<NotificationResponse>> {
        return userService.fetchNotifications(pageNumber, size).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchRecommendedUsers(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return userService.fetchRecommendedUsers(pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data.map { it.toUser() }
        }
    }

    override fun fetchConnectionRequests(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return userService.fetchConnectionRequests(userId, pageNumber, size, query)
            .map { response ->
                checkResponse(response)
                response.body()!!.data.map { it.toUser() }
            }
    }

    override fun acceptConnectionRequest(userId: String): Observable<ConnectUserResponse> {
        return userService.acceptConnectionRequest(UserConnectionBody(userId))
            .map { response ->
                checkResponse(response)
                response.body()!!.data
            }
    }

    override fun declineConnectionRequest(userId: String): Observable<Unit> {
        return userService.cancelConnectionRequest(UserConnectionBody(userId))
            .map { response ->
                checkResponse(response)
            }
    }

    override fun markNotificationRead(notificationId: String): Observable<Unit> {
        val notificationReadBody =
            NotificationReadBody(
                listOf(
                    DataItem(
                        type = "activity_records",
                        recordIds = listOf(notificationId)
                    )
                )
            )

        return userService.markNotificationRead(notificationReadBody)
            .map { response ->
                checkResponse(response)
            }
    }

    override fun fetchWorkExperiences(userId: String): Observable<List<WorkExperienceResponse>> {
        return userService.fetchWorkExperiences(userId).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun addNewExperience(
        userId: String,
        experience: WorkExperienceBody
    ): Observable<WorkExperienceResponse> {
        return userService.addNewExperience(userId, experience).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmation: String
    ): Observable<Unit> {
        return userService.changePassword(
            sessionRepository.getUserId(),
            ChangePasswordEntityBody(
                PasswordEntity(confirmation, newPassword, oldPassword)
            )
        ).map {
            checkResponse(it)
        }
    }
}
