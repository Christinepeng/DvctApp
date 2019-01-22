package com.divercity.android.db;

import android.arch.persistence.room.TypeConverter;

import com.divercity.android.core.utils.Util;

/**
 * Created by lucas on 02/01/2019.
 */

public class DateTypeConverter {

    @TypeConverter
    public static Long toLong(String value) {
        return Util.getMilisecFromStringDate(value);
    }

    @TypeConverter
    public static String toString(Long value) {
        return "pol";
    }
}
