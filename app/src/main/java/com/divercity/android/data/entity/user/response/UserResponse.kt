package com.divercity.android.data.entity.user.response

import android.content.Context
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.divercity.android.R
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.db.converter.CompanyTypeConverter
import com.divercity.android.db.converter.IntListTypeConverter
import com.divercity.android.db.converter.StringListTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user", primaryKeys = ["id"])
@TypeConverters(
    CompanyTypeConverter::class,
    IntListTypeConverter::class,
    StringListTypeConverter::class
)
data class UserResponse(

    @field:SerializedName("attributes")
    @Embedded(prefix = "attr_")
    var userAttributes: UserAttributes? = null,

    @field:SerializedName("id")
    var id: String = "-1",

    @field:SerializedName("type")
    var type: String? = "users"
) : ConnectionItem {

    fun isUserJobSeeker(context: Context): Boolean {
        val accountType = userAttributes?.accountType
        return accountType != null &&
                (accountType == context.getString(R.string.job_seeker_id) ||
                        accountType == context.getString(R.string.student_id) ||
                        accountType == context.getString(R.string.professional_id))
    }

     fun getUserType(): String? {
        return userAttributes?.accountType
    }

     fun getSkills(): List<String>? {
        return userAttributes?.skills
    }


}