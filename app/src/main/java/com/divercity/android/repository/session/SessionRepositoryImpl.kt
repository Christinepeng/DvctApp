package com.divercity.android.repository.session

import android.content.Context
import com.divercity.android.R
import com.divercity.android.core.sharedpref.SharedPreferencesManager
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.db.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Created by lucas on 24/10/2018.
 */

class SessionRepositoryImpl
constructor(
    val context: Context,
    val userDao: UserDao
) : SessionRepository {

    private val USER_PREF_NAME = "USER_PREF_NAME"
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var currentLoggedUser: UserResponse? = null

    enum class Key {
        ACCESS_TOKEN,
        CLIENT,
        UID,
        DEVICE_ID,
        USER_ID,
        FCM_TOKEN
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

    override fun clearUserData() {
        currentLoggedUser = null
        uiScope.launch {
            withContext(Dispatchers.IO) {
                userDao.deleteUser()
            }
        }
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

    override fun getAccountType(): String {
        return currentLoggedUser?.userAttributes?.accountType!!
    }

    override fun getUserName(): String {
        return currentLoggedUser?.userAttributes?.name!!
    }

    override fun getUserAvatarUrl(): String {
        return currentLoggedUser?.userAttributes?.avatarMedium!!
    }

    override fun getEthnicity(): String? {
        return currentLoggedUser?.userAttributes?.ethnicity
    }

    override fun getGender(): String? {
        return currentLoggedUser?.userAttributes?.gender
    }

    override fun getIndustries(): String? {
//        TODO LUCAS
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

    override fun saveUserHeaderData(response: Response<DataObject<UserResponse>>) {
        setClient(response.headers().get("client"))
        setUid(response.headers().get("uid"))
        setAccessToken(response.headers().get("access-token"))
        saveUserData(response.body()!!.data)
    }

    override fun saveUserData(user: UserResponse) {
        this.currentLoggedUser = user
        setUserId(user.id)
        uiScope.launch {
            withContext(Dispatchers.IO) {
                userDao.insertUser(user)
            }
        }
    }

    override fun setUserId(id: String) {
        sharedPreferencesManager.put(Key.USER_ID, id)
    }

    override fun isLoggedUserJobSeeker(): Boolean {
        val accountType = currentLoggedUser?.userAttributes?.accountType
        return accountType != null &&
                (accountType == context.getString(R.string.job_seeker_id) ||
                        accountType == context.getString(R.string.student_id) ||
                        accountType == context.getString(R.string.professional_id))
    }

    override fun getUserType(): String? {
        return currentLoggedUser?.userAttributes?.accountType
    }
}