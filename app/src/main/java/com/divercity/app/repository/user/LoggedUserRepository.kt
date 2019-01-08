package com.divercity.app.repository.user

import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.user.response.UserResponse
import retrofit2.Response

/**
 * Created by lucas on 26/10/2018.
 */

interface LoggedUserRepository {

    fun setAccessToken(token: String?)

    fun getAccessToken(): String?

    fun setClient(client: String?)

    fun getClient(): String?

    fun setUid(uid: String?)

    fun getUid(): String?

    fun setAvatarThumbUrl(url: String?)

    fun getAvatarThumbUrl(): String?

    fun setAvatarUrl(url: String?)

    fun getAvatarUrl(): String?

    fun setUserId(userId: String?)

    fun getUserId(): String?

    fun setFullName(userName: String?)

    fun getFullName(): String?

    fun setAccountType(accountType: String?)

    fun getAccountType(): String?

    fun isUserLogged(): Boolean

    fun clearUserData()

    fun saveUserHeaderData(response: Response<DataObject<UserResponse>>)

    fun saveUserData(data: DataObject<UserResponse>?)

    fun isLoggedUserJobSeeker(): Boolean

    fun getEthnicity(): String?

    fun setEthnicity(ethnicity: String?)

    fun getGender(): String?

    fun setGender(gender: String?)

    fun getIndustry(): String?

    fun setIndustry(industry: String?)

    fun getLocation(): String?

    fun setLocation(location: String?)

    fun getAgeRange(): String?

    fun setAgeRange(ageRange: String?)
}