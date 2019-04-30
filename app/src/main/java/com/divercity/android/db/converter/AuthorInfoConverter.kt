package com.divercity.android.db.converter

import androidx.room.TypeConverter
import com.divercity.android.data.entity.group.answer.response.AuthorInfoEntity
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object AuthorInfoConverter {

    @TypeConverter
    @JvmStatic
    fun authorInfoToString(value: AuthorInfoEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringToAuthorInfo(value: String): AuthorInfoEntity {
        return  Gson().fromJson(value, AuthorInfoEntity::class.java) as AuthorInfoEntity
    }
}