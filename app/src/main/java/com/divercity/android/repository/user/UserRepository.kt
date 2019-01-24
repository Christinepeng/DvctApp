package com.divercity.android.repository.user

import com.divercity.android.data.entity.device.body.DeviceBody
import com.divercity.android.data.entity.device.response.DeviceResponse
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody
import com.divercity.android.data.entity.profile.profile.User
import com.divercity.android.data.entity.user.followuser.FollowUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable

/**
 * Created by lucas on 18/10/2018.
 */

interface UserRepository : LoggedUserRepository {

    fun fetchRemoteUserData(userId: String): Observable<UserResponse>

    fun updateUserProfile(user: User): Observable<UserResponse>

    fun joinGroup(idGroup: String): Observable<Boolean>

    fun uploadUserPhoto(body: ProfilePictureBody): Observable<UserResponse>

    fun followOccupationOfInterests(occupationIds: List<String>): Observable<UserResponse>

    fun followInterests(interestsIds: List<String>): Observable<UserResponse>

    fun followIndustries(industriesIds: List<String>): Observable<UserResponse>

    fun followUser(userId: String): Observable<FollowUserResponse>

    fun unfollowUser(userId: String): Observable<Void>

    fun saveDevice(body: DeviceBody): Observable<DeviceResponse>

    fun updateDevice(deviceId: String, body: DeviceBody): Observable<Void>

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

    fun fetchUsers(pageNumber: Int,
                   size: Int,
                   query: String?
    ): Observable<List<UserResponse>>
}
