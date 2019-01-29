package com.divercity.android.db.converter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object StringListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun listStringToString(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringToListString(value: String): List<String> {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }
}