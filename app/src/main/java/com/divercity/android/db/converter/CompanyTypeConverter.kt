package com.divercity.android.db.converter

import android.arch.persistence.room.TypeConverter
import com.divercity.android.data.entity.user.response.Company
import com.google.gson.Gson

/**
 * Created by lucas on 16/01/2019.
 */

object CompanyTypeConverter {

    @TypeConverter
    @JvmStatic
    fun companyToString(value: Company): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringToCompany(value: String): Company {
        val company = Gson().fromJson(value, Company::class.java) as Company
        return company
    }
}