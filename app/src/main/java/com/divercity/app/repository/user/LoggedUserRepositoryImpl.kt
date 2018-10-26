package com.divercity.app.repository.user

import android.content.Context
import com.divercity.app.core.sharedpref.SharedPreferencesManager

/**
 * Created by lucas on 24/10/2018.
 */

class LoggedUserRepositoryImpl(context: Context) : LoggedUserRepository {

    private val USER_PREF_NAME = "USER_PREF_NAME"

    enum class Key {
        ACCESS_TOKEN,
        CLIENT,
        UID,
        AVATAR_URL,
        USER_ID,
        ACCOUNT_TYPE
    }

    private val sharedPreferencesManager: SharedPreferencesManager<Key> = SharedPreferencesManager(context, USER_PREF_NAME)

    override fun setAccessToken(token: String) {
        sharedPreferencesManager.put(Key.ACCESS_TOKEN, token)
    }

    override fun getAccessToken(): String? {
        return sharedPreferencesManager.getString(Key.ACCESS_TOKEN)
    }

    override fun setClient(client: String) {
        sharedPreferencesManager.put(Key.CLIENT, client)
    }

    override fun getClient(): String? {
        return sharedPreferencesManager.getString(Key.CLIENT)
    }

    override fun setUid(uid: String) {
        sharedPreferencesManager.put(Key.UID, uid)
    }

    override fun getUid(): String? {
        return sharedPreferencesManager.getString(Key.UID)
    }

    override fun setAvatarUrl(url: String) {
        sharedPreferencesManager.put(Key.AVATAR_URL, url)
    }

    override fun getAvatarUrl(): String? {
        return sharedPreferencesManager.getString(Key.AVATAR_URL)
    }

    override fun setUserId(userId: String) {
        sharedPreferencesManager.put(Key.USER_ID, userId)
    }

    override fun getUserId(): String? {
        return sharedPreferencesManager.getString(Key.USER_ID)
    }

    override fun setAccountType(accountType: String) {
        sharedPreferencesManager.put(Key.ACCOUNT_TYPE, accountType)
    }

    override fun getAccountType(): String? {
        return sharedPreferencesManager.getString(Key.ACCOUNT_TYPE)
    }

    override fun isUserLogged(): Boolean {
        return getAccessToken() != null && getUid() != null && getClient() != null
    }

    override fun clearUserData() {
        sharedPreferencesManager.clearAllData()
    }
}