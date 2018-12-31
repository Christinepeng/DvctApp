package com.divercity.app.data.entity.jobapplication

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Applicant(

	@field:SerializedName("account_type")
	val accountType: String? = null,

	@field:SerializedName("ethnicity")
	val ethnicity: String? = null,

	@field:SerializedName("student_majors")
	val studentMajors: List<String?>? = null,

	@field:SerializedName("industries")
	val industries: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("school_name")
	val schoolName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_followed_by_current")
	val isFollowedByCurrent: Boolean? = null,

	@field:SerializedName("photos")
	val photos: Photos? = null,

	@field:SerializedName("occupation_of_interest")
	val occupationOfInterest: List<String?>? = null,

	@field:SerializedName("email")
	val email: String? = null
)