package com.divercity.android.data.entity.user.response

import android.content.Context
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.divercity.android.R
import com.divercity.android.core.extension.empty
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.db.converter.CompanyTypeConverter
import com.divercity.android.db.converter.IntListTypeConverter
import com.divercity.android.db.converter.StringListTypeConverter
import com.divercity.android.model.user.User
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user", primaryKeys = ["id"])
@TypeConverters(
    CompanyTypeConverter::class,
    IntListTypeConverter::class,
    StringListTypeConverter::class
)
data class UserEntityResponse(

    @field:SerializedName("attributes")
    @Embedded(prefix = "attr_")
    var userAttributes: AttributesEntity? = AttributesEntity.empty(),

    @field:SerializedName("id")
    var id: String = String.empty(),

    @field:SerializedName("type")
    var type: String? = String.empty()
) : ConnectionItem {


    fun isUserJobSeeker(context: Context): Boolean {
        val accountType = userAttributes?.accountType
        return accountType != null &&
                (accountType == context.getString(R.string.professional_job_seeker_id) ||
                        accountType == context.getString(R.string.student_id) ||
                        accountType == context.getString(R.string.professional_id))
    }

    fun toUser() = User(
        id = id,
        country = userAttributes?.country,
        avatarMedium = userAttributes?.avatarMedium,
        accountType = userAttributes?.accountType,
        occupation = userAttributes?.occupation,
        birthdate = userAttributes?.birthdate,
        role = userAttributes?.role,
        gender = userAttributes?.gender,
        ethnicity = userAttributes?.ethnicity?.name,
        city = userAttributes?.city,
        timezone = userAttributes?.timezone,
        answersCount = userAttributes?.answersCount,
        ageRange = userAttributes?.ageRange,
        avatarThumb = userAttributes?.avatarThumb,
        companyId = userAttributes?.company?.id,
        companyName = userAttributes?.company?.name,
        connected = userAttributes?.connected,
        createdAt = userAttributes?.createdAt,
        email = userAttributes?.email,
        followersCount = userAttributes?.followersCount,
        followingCount = userAttributes?.followingCount,
        groupOfInterestFollowingCount = userAttributes?.groupOfInterestFollowingCount,
        industries = userAttributes?.industries,
        interestIds = userAttributes?.interestIds,
        isDefaultAvatar = userAttributes?.isDefaultAvatar,
        isFollowedByCurrent = userAttributes?.isFollowedByCurrent,
        lastName = userAttributes?.lastName,
        name = userAttributes?.name,
        nickname = userAttributes?.nickname,
        noPasswordSet = userAttributes?.noPasswordSet,
        occupationOfInterests = userAttributes?.occupationOfInterests,
        phoneNumber = userAttributes?.phonenumber,
        questionsCount = userAttributes?.questionsCount,
        schoolName = userAttributes?.schoolName,
        skills = userAttributes?.skills,
        studentMajors = userAttributes?.studentMajors,
        uid = userAttributes?.uid
    )

}