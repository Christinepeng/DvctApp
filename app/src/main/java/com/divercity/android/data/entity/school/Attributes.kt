package com.divercity.android.data.entity.school

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("questions_count")
	val questionsCount: Int? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("members_count")
	val membersCount: Int? = null
)