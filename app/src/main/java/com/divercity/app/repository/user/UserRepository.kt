package com.divercity.app.repository.user

import com.divercity.app.data.entity.user.response.UserResponse
import com.divercity.app.data.entity.profile.picture.ProfilePictureBody
import com.divercity.app.data.entity.profile.profile.User
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
