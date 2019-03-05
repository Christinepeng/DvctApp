package com.divercity.android.db.converter

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by lucas on 16/01/2019.
 */

object DateTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toLong(value: Date?): Long {
        return value?.time ?: Date().time
    }

    @TypeConverter
    @JvmStatic
    fun toString(value: Long): Date {
        return Date(value)
    }
}