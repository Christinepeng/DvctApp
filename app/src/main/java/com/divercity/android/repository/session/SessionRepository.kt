package com.divercity.android.repository.session

import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.user.response.UserResponse
import retrofit2.Response

/**
 * Created by lucas on 26/10/2018.
 */

interface SessionRepository {

    fun setAccessToken(token: String?)

    fun getAccessToken(): String?

    fun setClient(client: String?)

    fun getClient(): String?

    fun setUid(uid: String?)

    fun getUid(): String?

    fun isUserLogged(): Boolean

    fun clearUserData()

    fun saveUserHeaderData(response: Response<DataObject<UserResponse>>)

    fun saveUserData(user: UserResponse)

    fun isLoggedUserJobSeeker(): Boolean

    fun getDeviceId(): String?

    fun setDeviceId(deviceId: String?)

    fun getFCMToken(): String?

    fun setFCMToken(token: String?)

    fun getUserId(): String

    fun setUserId(id : String)

    fun getUserName(): String

    fun getEmail() : String?

    fun getUserAvatarUrl(): String

    fun getAccountType(): String

    fun getEthnicity(): String?

    fun getGender(): String?

    fun getIndustries(): String?

    fun getAgeRange(): String?

    fun getLocation(): String?

    fun getInterests() : List<Int>?

    fun getUserType() : String?
}