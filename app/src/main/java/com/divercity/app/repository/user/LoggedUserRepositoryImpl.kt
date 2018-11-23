package com.divercity.app.repository.user

import android.content.Context
import com.divercity.app.R
import com.divercity.app.core.sharedpref.SharedPreferencesManager
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.login.response.LoginResponse
import retrofit2.Response

/**
 * Created by lucas on 24/10/2018.
 */

class LoggedUserRepositoryImpl(val context: Context) : LoggedUserRepository {

    private val USER_PREF_NAME = "USER_PREF_NAME"

    enum class Key {
        ACCESS_TOKEN,
        CLIENT,
        UID,
        AVATAR_THUMB_URL,
        AVATAR_MED_URL,
        OCCUPATION,
        LOCATION,
        FULL_NAME,
        USER_ID,
        ACCOUNT_TYPE
    }

    private val sharedPreferencesManager: SharedPreferencesManager<Key> =
            SharedPreferencesManager(context, USER_PREF_NAME)

    override fun setAccessToken(token: String?) {
        sharedPreferencesManager.put(Key.ACCESS_TOKEN, token)
    }

    override fun getAccessToken(): String? {
        return sharedPreferencesManager.getString(Key.ACCESS_TOKEN)
    }

    override fun setClient(client: String?) {
        sharedPreferencesManager.put(Key.CLIENT, client)
    }

    override fun getClient(): String? {
        return sharedPreferencesManager.getString(Key.CLIENT)
    }

    override fun setUid(uid: String?) {
        sharedPreferencesManager.put(Key.UID, uid)
    }

    override fun getUid(): String? {
        return sharedPreferencesManager.getString(Key.UID)
    }

    override fun setAvatarThumbUrl(url: String?) {
        sharedPreferencesManager.put(Key.AVATAR_THUMB_URL, url)
    }

    override fun getAvatarThumbUrl(): String? {
        return sharedPreferencesManager.getString(Key.AVATAR_THUMB_URL)
    }

    override fun setUserId(userId: String?) {
        sharedPreferencesManager.put(Key.USER_ID, userId)
    }

    override fun getUserId(): String? {
        return sharedPreferencesManager.getString(Key.USER_ID)
    }

    override fun setAccountType(accountType: String?) {
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

    override fun setAvatarUrl(url: String?) {
        sharedPreferencesManager.put(Key.AVATAR_MED_URL, url)
    }

    override fun getAvatarUrl(): String? {
        return sharedPreferencesManager.getString(Key.AVATAR_MED_URL)
    }

    override fun getFullName(): String? {
        return sharedPreferencesManager.getString(Key.FULL_NAME)
    }

    override fun setFullName(userName: String?) {
        sharedPreferencesManager.put(Key.FULL_NAME, userName)
    }

    override fun saveUserHeaderData(response: Response<DataObject<LoginResponse>>) {
        setClient(response.headers().get("client"))
        setUid(response.headers().get("uid"))
        setAccessToken(response.headers().get("access-token"))
        saveUserData(response.body())
    }

    override fun saveUserData(data: DataObject<LoginResponse>?) {
        setAvatarThumbUrl(data?.data?.attributes?.avatarThumb)
        setAvatarUrl(data?.data?.attributes?.avatarMedium)
        setUserId(data?.data?.id)
        setAccountType(data?.data?.attributes?.accountType)
        setFullName(data?.data?.attributes?.name)
    }

    override fun isLoggedUserJobSeeker(): Boolean {
        return getAccountType() != null &&
                (getAccountType().equals(context.getString(R.string.job_seeker_id)) ||
                        getAccountType().equals(context.getString(R.string.student_id))) ||
                getAccountType().equals(context.getString(R.string.professional_id))
    }
}