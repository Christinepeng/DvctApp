package com.divercity.android.core.sharedpref

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by lucas on 24/10/2018.
 */

class SharedPreferencesManager<T : Enum<*>>(context: Context, prefName: String) {

    private val mPref: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor
        get() = mPref.edit()

    fun clearAllData() = editor.clear().commit()

    //    PUTS

    fun put(key: T, value: String?) = editor.putString(key.name, value).commit()

    fun put(key: T, value: Int) = editor.putInt(key.name, value).commit()

    fun put(key: T, value: Long) = editor.putLong(key.name, value).commit()

    fun put(key: T, value: Boolean) = editor.putBoolean(key.name, value).commit()

    fun put(key: T, value: MutableSet<String>) = editor.putStringSet(key.name, value).commit()

    //    GETS

    fun getInt(key: T): Int = mPref.getInt(key.name, -1)

    fun getString(key: T, defaultValue: String): String? = mPref.getString(key.name, defaultValue)

    fun getString(key: T): String? = mPref.getString(key.name, null)

    fun getBoolean(key: T): Boolean = mPref.getBoolean(key.name, false)

    fun getLong(key: T): Long = mPref.getLong(key.name, -1)

    fun getSet(key: T): MutableSet<String>? = mPref.getStringSet(key.name, null)
}