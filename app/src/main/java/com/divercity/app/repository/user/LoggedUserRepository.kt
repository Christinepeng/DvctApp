package com.divercity.app.repository.user

/**
 * Created by lucas on 26/10/2018.
 */

interface LoggedUserRepository {

    fun setAccessToken(token: String)

    fun getAccessToken(): String?

    fun setClient(client: String)

    fun getClient(): String?

    fun setUid(uid: String)

    fun getUid(): String?

    fun setAvatarUrl(url: String)

    fun getAvatarUrl(): String?

    fun setUserId(userId: String)

    fun getUserId(): String?

    fun setAccountType(accountType: String)

    fun getAccountType(): String?

    fun isUserLogged(): Boolean

    fun clearUserData()
}