package com.divercity.android.db.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by lucas on 16/01/2019.
 */

object DateTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toLong(value: Date): Long {
        return value.time
    }

    @TypeConverter
    @JvmStatic
    fun toString(value: Long): Date {
        return Date(value)
    }
}