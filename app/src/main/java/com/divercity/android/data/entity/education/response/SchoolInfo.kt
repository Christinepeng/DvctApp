package com.divercity.android.data.entity.education.response

import com.google.gson.annotations.SerializedName

data class SchoolInfo(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("members_count")
	val membersCount: Int? = null,

	@field:SerializedName("id")
	val id: String? = null
)