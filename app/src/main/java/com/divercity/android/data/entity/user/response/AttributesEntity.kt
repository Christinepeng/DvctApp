package com.divercity.android.data.entity.user.response

import androidx.room.Embedded
import com.divercity.android.core.extension.empty
import com.google.gson.annotations.SerializedName
import java.util.*

data class AttributesEntity(

    @field:SerializedName("country")
    var country: String? = String.empty(),

    @field:SerializedName("avatar_medium")
    var avatarMedium: String? = String.empty(),

    @field:SerializedName("account_type")
    var accountType: String? = String.empty(),

    @field:SerializedName("occupation")
    var occupation: String? = String.empty(),

    @field:SerializedName("birthdate")
    var birthdate: String? = String.empty(),

    @field:SerializedName("role")
    var role: String? = String.empty(),

    @field:SerializedName("gender")
    var gender: String? = String.empty(),

    @field:SerializedName("ethnicity_info")
    @Embedded(prefix = "ethn_")
    var ethnicity: EthnicityInfoEntity? = null,

    @field:SerializedName("city")
    var city: String? = String.empty(),

    @field:SerializedName("timezone")
    var timezone: String? = String.empty(),

    @field:SerializedName("phonenumber")
    var phonenumber: String? = String.empty(),

    @field:SerializedName("created_at")
    var createdAt: String? = String.empty(),

    @field:SerializedName("questions_count")
    var questionsCount: Int? = -1,

    @field:SerializedName("occupation_of_interests")
    var occupationOfInterests: List<String>? = Collections.emptyList(),

    @field:SerializedName("group_of_interest_following_count")
    var groupOfInterestFollowingCount: Int? = -1,

    @field:SerializedName("uid")
    var uid: String? = String.empty(),

    @field:SerializedName("answers_count")
    var answersCount: Int? = -1,

    @field:SerializedName("nickname")
    var nickname: String? = String.empty(),

    @field:SerializedName("company")
    var company: CompanyEntity? = CompanyEntity.empty(),

    @field:SerializedName("is_followed_by_current")
    var isFollowedByCurrent: Boolean? = false,

    @field:SerializedName("email")
    var email: String? = String.empty(),

    @field:SerializedName("lat")
    var lat: String? = String.empty(),

    @field:SerializedName("no_password_set")
    var noPasswordSet: String? = String.empty(),

    @field:SerializedName("lng")
    var lng: String? = String.empty(),

    @field:SerializedName("avatar_thumb")
    var avatarThumb: String? = String.empty(),

    @field:SerializedName("last_name")
    var lastName: String? = String.empty(),

    @field:SerializedName("school_name")
    var schoolName: String? = String.empty(),

    @field:SerializedName("is_default_avatar")
    var isDefaultAvatar: Boolean? = false,

    @field:SerializedName("interest_ids")
    var interestIds: List<Int>? = Collections.emptyList(),

    @field:SerializedName("following_count")
    var followingCount: Int? = -1,

    @field:SerializedName("followers_count")
    var followersCount: Int? = -1,

    @field:SerializedName("industries")
    var industries: List<String>? = Collections.emptyList(),

    @field:SerializedName("student_majors")
    var studentMajors: List<String>? = Collections.emptyList(),

    @field:SerializedName("name")
    var name: String? = String.empty(),

    @field:SerializedName("age_range")
    var ageRange: String? = String.empty(),

    @field:SerializedName("skills")
    var skills: List<String>? = Collections.emptyList(),

    @field:SerializedName("connected")
    var connected: String? = String.empty()
) {
    companion object {

        fun empty() = AttributesEntity()
    }
}