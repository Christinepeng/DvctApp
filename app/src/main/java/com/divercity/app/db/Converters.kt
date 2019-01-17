package com.divercity.app.db

import android.arch.persistence.room.TypeConverter
import com.divercity.app.data.entity.createchat.UsersItem
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object Converters {

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