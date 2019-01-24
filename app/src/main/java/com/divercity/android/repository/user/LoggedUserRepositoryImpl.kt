package com.divercity.android.repository.user

import android.content.Context
import com.divercity.android.R
import com.divercity.android.core.sharedpref.SharedPreferencesManager
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.user.response.UserResponse
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
        ACCOUNT_TYPE,
        ETHNICITY,
        GENDER,
        INDUSTRY,
        AGE_RANGE,
        DEVICE_ID,
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

    override fun getEthnicity(): String? {
        return sharedPreferencesManager.getString(Key.ETHNICITY)
    }

    override fun setEthnicity(ethnicity: String?) {
        sharedPreferencesManager.put(Key.ETHNICITY, ethnicity)
    }

    override fun getGender(): String? {
        return sharedPreferencesManager.getString(Key.GENDER)
    }

    override fun setGender(gender: String?) {
        sharedPreferencesManager.put(Key.GENDER, gender)
    }

    override fun getIndustry(): String? {
        return sharedPreferencesManager.getString(Key.INDUSTRY)
    }

    override fun setIndustry(industry: String?) {
        sharedPreferencesManager.put(Key.INDUSTRY, industry)
    }

    override fun getLocation(): String? {
        return sharedPreferencesManager.getString(Key.LOCATION)
    }

    override fun setLocation(location: String?) {
        sharedPreferencesManager.put(Key.LOCATION, location)
    }

    override fun getAgeRange(): String? {
        return sharedPreferencesManager.getString(Key.AGE_RANGE)
    }

    override fun setAgeRange(ageRange: String?) {
        sharedPreferencesManager.put(Key.AGE_RANGE, ageRange)
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

    override fun saveUserHeaderData(response: Response<DataObject<UserResponse>>) {
        setClient(response.headers().get("client"))
        setUid(response.headers().get("uid"))
        setAccessToken(response.headers().get("access-token"))
        saveUserData(response.body())
    }

    override fun saveUserData(data: DataObject<UserResponse>?) {
        setUserId(data?.data?.id)
        data?.data?.userAttributes?.apply {
            setAvatarThumbUrl(avatarThumb)
            setAvatarUrl(avatarMedium)
            setAccountType(accountType)
            setFullName(name)
            setEthnicity(ethnicity)
            setGender(gender)
            setIndustry(Util.getStringFromArray(industries))

            if (city != null && country != null)
                setLocation(city.plus(", ").plus(country))

            setAgeRange(ageRange)
        }
    }

    override fun isLoggedUserJobSeeker(): Boolean {
        return getAccountType() != null &&
                (getAccountType().equals(context.getString(R.string.job_seeker_id)) ||
                        getAccountType().equals(context.getString(R.string.student_id))) ||
                getAccountType().equals(context.getString(R.string.professional_id))
    }
}