package com.divercity.android.db.converter

import android.arch.persistence.room.TypeConverter
import com.divercity.android.data.entity.group.answer.response.AuthorInfo
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object AuthorInfoConverter {

    @TypeConverter
    @JvmStatic
    fun authorInfoToString(value: AuthorInfo): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringToAuthorInfo(value: String): AuthorInfo {
        return  Gson().fromJson(value, AuthorInfo::class.java) as AuthorInfo
    }
}