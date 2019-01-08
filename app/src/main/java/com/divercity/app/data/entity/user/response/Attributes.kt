package com.divercity.app.data.entity.user.response

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("avatar_medium")
	val avatarMedium: String? = null,

	@field:SerializedName("account_type")
	val accountType: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("ethnicity")
	val ethnicity: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("phonenumber")
	val phonenumber: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("questions_count")
	val questionsCount: Int? = null,

	@field:SerializedName("occupation_of_interests")
	val occupationOfInterests: List<String?>? = null,

	@field:SerializedName("group_of_interest_following_count")
	val groupOfInterestFollowingCount: Int? = null,

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("answers_count")
	val answersCount: Int? = null,

	@field:SerializedName("nickname")
	val nickname: String? = null,

	@field:SerializedName("company")
	val company: Company? = null,

	@field:SerializedName("is_followed_by_current")
	val isFollowedByCurrent: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("no_password_set")
	val noPasswordSet: String? = null,

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("avatar_thumb")
	val avatarThumb: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("school_name")
	val schoolName: String? = null,

	@field:SerializedName("is_default_avatar")
	val isDefaultAvatar: Boolean? = null,

	@field:SerializedName("interest_ids")
	val interestIds: List<Int?>? = null,

	@field:SerializedName("following_count")
	val followingCount: Int? = null,

	@field:SerializedName("followers_count")
	val followersCount: Int? = null,

	@field:SerializedName("industries")
	val industries: List<String?>? = null,

	@field:SerializedName("student_majors")
	val studentMajors: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("age_range")
	val ageRange: String? = null
)