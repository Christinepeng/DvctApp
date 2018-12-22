package com.divercity.app.repository.user

import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.data.entity.profile.picture.ProfilePictureBody
import com.divercity.app.data.entity.profile.profile.User

import io.reactivex.Observable

/**
 * Created by lucas on 18/10/2018.
 */

interface UserRepository : LoggedUserRepository {

    fun fetchRemoteUserData(userId: String): Observable<LoginResponse>

    fun updateUserProfile(user: User): Observable<LoginResponse>

    fun joinGroup(idGroup: String): Observable<Boolean>

    fun uploadUserPhoto(body: ProfilePictureBody): Observable<LoginResponse>

    fun followOccupationOfInterests(occupationIds: List<String>): Observable<LoginResponse>

    fun followInterests(interestsIds: List<String>): Observable<LoginResponse>

    fun followIndustries(industriesIds: List<String>): Observable<LoginResponse>

    fun fetchFollowersByUser(
            userId: String,
            pageNumber: Int,
            size: Int,
            query: String?
    ): Observable<List<LoginResponse>>

    fun fetchFollowingByUser(
        userId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<LoginResponse>>
}
