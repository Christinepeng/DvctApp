package com.divercity.app.core.base;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager<T extends Enum> {

    private SharedPreferences mPref;

    public SharedPreferencesManager(Context context, String prefName) {
        mPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return mPref.edit();
    }

    public void clearAllData() {
        getEditor().clear().commit();
    }

    //    PUTS

    public void put(T key, String val) {
        getEditor().putString(key.name(), val).commit();
    }

    public void put(T key, int val) {
        getEditor().putInt(key.name(), val).commit();
    }

    public void put(T key, long val) {
        getEditor().putLong(key.name(), val).commit();
    }

    //    GETS

    public int getInt(T key) {
        return mPref.getInt(key.name(), -1);
    }

    public String getString(T key, String defaultValue) {
        return mPref.getString(key.name(), defaultValue);
    }

    public String getString(T key) {
        return mPref.getString(key.name(), null);
    }

    public long getLong(T key) {
        return mPref.getLong(key.name(), -1);
    }

}
