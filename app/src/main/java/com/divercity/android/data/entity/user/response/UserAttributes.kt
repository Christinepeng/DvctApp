package com.divercity.android.data.entity.user.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserAttributes (

	@field:SerializedName("country")
	var country: String? = "",

	@field:SerializedName("avatar_medium")
	var avatarMedium: String? = "",

	@field:SerializedName("account_type")
	var accountType: String? = "",

	@field:SerializedName("occupation")
	var occupation: String? = "",

	@field:SerializedName("birthdate")
	var birthdate: String? = "",

	@field:SerializedName("role")
	var role: String? = "",

	@field:SerializedName("gender")
	var gender: String? = "",

	@field:SerializedName("ethnicity")
	var ethnicity: String? = "",

	@field:SerializedName("city")
	var city: String? = "",

	@field:SerializedName("timezone")
	var timezone: String? = "",

	@field:SerializedName("phonenumber")
	var phonenumber: String? = "",

	@field:SerializedName("created_at")
	var createdAt: String? = "",

	@field:SerializedName("questions_count")
	var questionsCount: Int? = -1,

	@field:SerializedName("occupation_of_interests")
	var occupationOfInterests: List<String>? = Collections.emptyList(),

	@field:SerializedName("group_of_interest_following_count")
	var groupOfInterestFollowingCount: Int? = -1,

	@field:SerializedName("uid")
	var uid: String? = "",

	@field:SerializedName("answers_count")
	var answersCount: Int? = -1,

	@field:SerializedName("nickname")
	var nickname: String? = "",

	@field:SerializedName("company")
	var company: Company? = Company(),

	@field:SerializedName("is_followed_by_current")
	var isFollowedByCurrent: Boolean? = false,

	@field:SerializedName("email")
	var email: String? = "",

	@field:SerializedName("lat")
	var lat: String? = "",

	@field:SerializedName("no_password_set")
	var noPasswordSet: String? = "",

	@field:SerializedName("lng")
	var lng: String? = "",

	@field:SerializedName("avatar_thumb")
	var avatarThumb: String? = "",

	@field:SerializedName("last_name")
	var lastName: String? = "",

	@field:SerializedName("school_name")
	var schoolName: String? = "",

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
	var name: String? = "",

	@field:SerializedName("age_range")
	var ageRange: String? = "",

	@field:SerializedName("skills")
	var skills: List<String>? = Collections.emptyList()
)