package com.divercity.android.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object IntListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun listIntToString(value: List<Int>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringToListInt(value: String): List<Int> {
        val objects = Gson().fromJson(value, Array<Int>::class.java) as Array<Int>
        return objects.toList()
    }
}