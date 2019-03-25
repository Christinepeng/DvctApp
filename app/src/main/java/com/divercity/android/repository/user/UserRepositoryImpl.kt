package com.divercity.android.repository.user

import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.data.entity.activity.notificationread.DataItem
import com.divercity.android.data.entity.activity.notificationread.NotificationReadBody
import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.data.entity.device.response.DeviceResponse
import com.divercity.android.data.entity.industry.body.FollowIndustryBody
import com.divercity.android.data.entity.interests.body.FollowInterestsBody
import com.divercity.android.data.entity.occupationofinterests.body.FollowOOIBody
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody
import com.divercity.android.data.entity.profile.profile.User
import com.divercity.android.data.entity.profile.profile.UserProfileBody
import com.divercity.android.data.entity.user.connectuser.body.UserConnectionBody
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.services.UserService
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

    override fun fetchRemoteUserData(userId: String): Observable<UserResponse> {
        return userService.fetchUserData(userId)
                .map { response ->
                    checkResponse(response)
                    response.body()!!.data
                }
    }

    override fun joinGroup(groupId: String): Observable<Boolean> {
        return userService.joinGroup(groupId).map { response ->
            checkResponse(response)
            true
        }
    }

    override fun uploadUserPhoto(body: ProfilePictureBody): Observable<UserResponse> {
        return userService.uploadUserPhoto(body).map { response ->
            checkResponse(response)
            sessionRepository.saveUserData(response.body()!!.data)
            response.body()!!.data
        }
    }

    override fun updateLoggedUserProfile(user: User): Observable<UserResponse> {
        val userProfileBody = UserProfileBody()
        userProfileBody.user = user
        return userService.updateUserProfile(sessionRepository.getUserId(), userProfileBody)
                .map { response ->
                    checkResponse(response)
                    sessionRepository.saveUserData(response.body()!!.data)
                    response.body()!!.data
                }
    }

    override fun followOccupationOfInterests(occupationIds: List<String>): Observable<UserResponse> {
        return userService.followOccupationOfInterests(FollowOOIBody(occupationIds))
                .map { response ->
                    checkResponse(response)
                    response.body()!!.data
                }
    }

    override fun followInterests(interestsIds: List<String>): Observable<UserResponse> {
        return userService.followInterests(FollowInterestsBody(interestsIds)).map { response ->
            checkResponse(response)
            sessionRepository.saveUserData(response.body()!!.data)
            response.body()!!.data
        }
    }

    override fun followIndustries(industriesIds: List<String>): Observable<UserResponse> {
        return userService.followIndustries(FollowIndustryBody(industriesIds)).map { response ->
            checkResponse(response)
            sessionRepository.saveUserData(response.body()!!.data)
            response.body()!!.data
        }
    }

    override fun fetchFollowersByUser(
            userId: String,
            pageNumber: Int,
            size: Int,
            query: String?
    ): Observable<List<UserResponse>> {
        return userService.fetchFollowersByUser(userId, pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchFollowingByUser(
            userId: String,
            pageNumber: Int,
            size: Int,
            query: String?
    ): Observable<List<UserResponse>> {
        return userService.fetchFollowingByUser(userId, pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchUsers(
            pageNumber: Int,
            size: Int,
            query: String?
    ): Observable<List<UserResponse>> {
        return userService.fetchUsers(pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun connectUser(userId: String): Observable<ConnectUserResponse> {
        val partUserId = RequestBody.create(MediaType.parse("text/plain"), userId)
        return userService.connectUser(partUserId).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun unfollowUser(userId: String): Observable<Void> {
        val partUserId = RequestBody.create(MediaType.parse("text/plain"), userId)
        return userService.unfollowUser(partUserId).map { response ->
            checkResponse(response)
            response.body()
        }
    }

    override fun saveDevice(body: DeviceBody): Observable<DeviceResponse> {
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
    ): Observable<List<UserResponse>> {
        return userService.fetchRecommendedUsers(pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchConnectionRequests(
            userId: String,
            pageNumber: Int,
            size: Int,
            query: String?
    ): Observable<List<UserResponse>> {
        return userService.fetchConnectionRequests(userId, pageNumber, size, query)
                .map { response ->
                    checkResponse(response)
                    response.body()!!.data
                }
    }

    override fun acceptConnectionRequest(userId: String): Observable<ConnectUserResponse> {
        return userService.acceptConnectionRequest(UserConnectionBody(userId))
                .map { response ->
                    checkResponse(response)
                    response.body()!!.data
                }
    }

    override fun declineConnectionRequest(userId: String): Observable<ConnectUserResponse> {
        return userService.cancelConnectionRequest(UserConnectionBody(userId))
                .map { response ->
                    checkResponse(response)
                    response.body()!!.data
                }
    }

    override fun markNotificationRead(notificationId: String): Observable<Unit> {
        val notificationReadBody =
                NotificationReadBody(
                        listOf(DataItem(
                                type = "activity_records",
                                recordIds = listOf(notificationId)
                        )))

        return userService.markNotificationRead(notificationReadBody)
                .map { response ->
                    checkResponse(response)
                }
    }
}
