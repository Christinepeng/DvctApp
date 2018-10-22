package com.divercity.app.sharedpreference;

import android.content.Context;

import com.divercity.app.core.base.SharedPreferencesManager;

public class UserSharedPreferencesRepository {

    private static final String USER_PREF_NAME = "USER_PREF_NAME";

    public enum Key {
        ACCESS_TOKEN,
        CLIENT,
        UID,
        AVATAR_URL,
        USER_ID,
        ACCOUNT_TYPE
    }

    private SharedPreferencesManager<Key> sharedPreferencesManager;

    public UserSharedPreferencesRepository(Context context){
        this.sharedPreferencesManager = new SharedPreferencesManager<>(context, USER_PREF_NAME);
    }

    public void setAccessToken(String token){
        sharedPreferencesManager.put(Key.ACCESS_TOKEN,token);
    }

    public String getAccessToken(){
        return sharedPreferencesManager.getString(Key.ACCESS_TOKEN);
    }

    public void setClient(String client){
        sharedPreferencesManager.put(Key.CLIENT, client);
    }

    public String getClient() {
        return sharedPreferencesManager.getString(Key.CLIENT);
    }

    public void setUid(String uid){
        sharedPreferencesManager.put(Key.UID,uid);
    }

    public String getUid(){
        return sharedPreferencesManager.getString(Key.UID);
    }

    public void setAvatarUrl(String url){
        sharedPreferencesManager.put(Key.AVATAR_URL,url);
    }

    public String getAvatarUrl(){
        return sharedPreferencesManager.getString(Key.AVATAR_URL);
    }

    public void setUserId(String userId){
        sharedPreferencesManager.put(Key.USER_ID,userId);
    }

    public String getUserId(){
        return sharedPreferencesManager.getString(Key.USER_ID);
    }

    public void setAccountType(String accountType){
        sharedPreferencesManager.put(Key.ACCOUNT_TYPE,accountType);
    }

    public String getAccountType(){
        return sharedPreferencesManager.getString(Key.ACCOUNT_TYPE);
    }

    public boolean isUserLogged(){
        return getAccessToken() != null && getUid() != null && getClient() != null;
    }

    public void clearUserData(){
        sharedPreferencesManager.clearAllData();
    }

}
