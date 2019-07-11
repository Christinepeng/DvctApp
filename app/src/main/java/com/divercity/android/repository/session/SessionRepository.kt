package com.divercity.android.repository.session

import androidx.lifecycle.LiveData
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.user.response.UserEntityResponse
import com.divercity.android.model.user.User
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

    suspend fun clearUserData()

    fun getUserDB() : LiveData<User>

    fun saveUserHeaderData(response: Response<DataObject<UserEntityResponse>>)

    fun saveUserData(user: UserEntityResponse)

    fun isLoggedUserJobSeeker(): Boolean

    fun getDeviceId(): String?

    fun setDeviceId(deviceId: String?)

    fun getFCMToken(): String?

    fun setFCMToken(token: String?)

    fun getCurrentChatId(): String?

    fun setCurrentChatId(chatId: String?)

    fun getUserId(): String

    fun setUserId(id: String)

    fun getUserName(): String?

    fun getEmail(): String?

    fun getUserAvatarUrl(): String?

    fun getAccountType(): String

    fun getGender(): String?

    fun getIndustries(): String?

    fun getAgeRange(): String?

    fun getLocation(): String?

    fun getInterests(): List<Int>?

    fun getSkills(): List<String>?

    fun getUserType(): String?

    fun setDeepLinkGroupId(groupId: String?)

    fun getDeepLinkGroupId(): String?

    fun setDeepLinkType(type: String?)

    fun getDeepLinkType(): String?

    fun clearDeepLinkData()
}