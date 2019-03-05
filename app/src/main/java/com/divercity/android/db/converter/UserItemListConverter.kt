package com.divercity.android.db.converter

import androidx.room.TypeConverter
import com.divercity.android.data.entity.createchat.UsersItem
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object UserItemListConverter {

    @TypeConverter
    @JvmStatic
    fun chatsToJson(value: List<UsersItem>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToChats(value: String): List<UsersItem>? {
        val objects = Gson().fromJson(value, Array<UsersItem>::class.java) as Array<UsersItem>
        return objects.toList()
    }
}