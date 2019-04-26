package com.divercity.android.repository.session

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.crashlytics.android.Crashlytics
import com.divercity.android.BuildConfig
import com.divercity.android.core.sharedpref.SharedPreferencesManager
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.user.response.UserEntityResponse
import com.divercity.android.db.dao.UserDao
import com.divercity.android.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Created by lucas on 24/10/2018.
 */

class SessionRepositoryImpl
constructor(
    private val context: Context,
    private val userDao: UserDao
) : SessionRepository {

    private val USER_PREF_NAME = "USER_PREF_NAME"
    private var currentLoggedUser: UserEntityResponse? = null

    enum class Key {
        ACCESS_TOKEN,
        CLIENT,
        UID,
        DEVICE_ID,
        USER_ID,
        FCM_TOKEN,
        DEEP_LINK_TYPE,
        DEEP_LINK_GROUP_INVITE,
        CURRENT_CHAT_ID
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

    override fun isUserLogged(): Boolean {
        return getAccessToken() != null && getUid() != null && getClient() != null
    }

    override suspend fun clearUserData() {
        currentLoggedUser = null
        sharedPreferencesManager.clearAllData()
    }

    override fun getDeviceId(): String? {
        return sharedPreferencesManager.getString(Key.DEVICE_ID)
    }

    override fun setDeviceId(deviceId: String?) {
        sharedPreferencesManager.put(Key.DEVICE_ID, deviceId)
    }

    override fun getFCMToken(): String? {
        return sharedPreferencesManager.getString(Key.FCM_TOKEN)
    }

    override fun setFCMToken(token: String?) {
        sharedPreferencesManager.put(Key.FCM_TOKEN, token)
    }

    override fun getUserId(): String {
        return sharedPreferencesManager.getString(Key.USER_ID)!!
    }

    override fun getCurrentChatId(): String? {
        return sharedPreferencesManager.getString(Key.CURRENT_CHAT_ID)
    }

    override fun setCurrentChatId(chatId: String?) {
        sharedPreferencesManager.put(Key.CURRENT_CHAT_ID, chatId)
    }

    override fun setDeepLinkType(type: String?) {
        sharedPreferencesManager.put(Key.DEEP_LINK_TYPE, type)
    }

    override fun getDeepLinkType(): String? {
        return sharedPreferencesManager.getString(Key.DEEP_LINK_TYPE)
    }

    override fun setDeepLinkGroupId(groupId: String?) {
        sharedPreferencesManager.put(Key.DEEP_LINK_GROUP_INVITE, groupId)
    }

    override fun getDeepLinkGroupId(): String? {
        return sharedPreferencesManager.getString(Key.DEEP_LINK_GROUP_INVITE)
    }

    override fun clearDeepLinkData() {
        sharedPreferencesManager.put(Key.DEEP_LINK_TYPE, null)
        sharedPreferencesManager.put(Key.DEEP_LINK_GROUP_INVITE, null)
    }

    override fun getAccountType(): String {
        return currentLoggedUser?.userAttributes?.accountType ?: "error"
    }

    override fun getUserName(): String? {
        return currentLoggedUser?.userAttributes?.name
    }

    override fun getUserAvatarUrl(): String? {
        return currentLoggedUser?.userAttributes?.avatarMedium
    }

    override fun getEthnicity(): String? {
        return currentLoggedUser?.userAttributes?.ethnicity
    }

    override fun getGender(): String? {
        return currentLoggedUser?.userAttributes?.gender
    }

    override fun getIndustries(): String? {
//        TODO
        return ""
    }

    override fun getInterests(): List<Int>? {
        return currentLoggedUser?.userAttributes?.interestIds
    }

    override fun getAgeRange(): String? {
        return currentLoggedUser?.userAttributes?.ageRange
    }

    override fun getLocation(): String? {
        return currentLoggedUser?.userAttributes?.city.plus(", ")
            .plus(currentLoggedUser?.userAttributes?.country)
    }

    override fun getEmail(): String? {
        return currentLoggedUser?.userAttributes?.email
    }

    override fun saveUserHeaderData(response: Response<DataObject<UserEntityResponse>>) {
        setClient(response.headers().get("client"))
        setUid(response.headers().get("uid"))
        setAccessToken(response.headers().get("access-token"))
        saveUserData(response.body()!!.data)
    }

    override fun saveUserData(user: UserEntityResponse) {
        this.currentLoggedUser = user
        setUserId(user.id)

        // Crashlytics additional data
        if (!BuildConfig.DEBUG) {
            Crashlytics.setUserEmail(user.userAttributes?.email)
            Crashlytics.setUserName(user.userAttributes?.name)
            Crashlytics.setUserIdentifier(user.id)
            Crashlytics.setString("ACCOUNT_TYPE", user.userAttributes?.accountType)
        }

        CoroutineScope(Dispatchers.IO).launch {
            userDao.insertUser(user)
        }
    }

    override fun setUserId(id: String) {
        sharedPreferencesManager.put(Key.USER_ID, id)
    }

    override fun isLoggedUserJobSeeker(): Boolean {
        return currentLoggedUser?.isUserJobSeeker(context) ?: true
    }

    override fun getUserType(): String? {
        return currentLoggedUser?.userAttributes?.accountType
    }

    override fun getSkills(): List<String>? {
        return currentLoggedUser?.userAttributes?.skills
    }

    override fun getUserDB(): LiveData<User> {
        return Transformations.map(userDao.getUser()) {
            it.toUser()
        }
    }
}