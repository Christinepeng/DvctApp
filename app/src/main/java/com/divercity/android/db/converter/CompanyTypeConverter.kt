package com.divercity.android.db.converter

import androidx.room.TypeConverter
import com.divercity.android.data.entity.user.response.CompanyEntity
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object CompanyTypeConverter {

    @TypeConverter
    @JvmStatic
    fun companyToString(value: CompanyEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringToCompany(value: String): CompanyEntity {
        return  Gson().fromJson(value, CompanyEntity::class.java) as CompanyEntity
    }
}