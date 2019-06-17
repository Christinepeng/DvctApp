package com.divercity.android.data.entity.education.response

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("qualification")
	val qualification: String? = null,

	@field:SerializedName("school_info")
	val schoolInfo: SchoolInfo? = null,

	@field:SerializedName("start_year")
	val startYear: String? = null,

	@field:SerializedName("end_year")
	val endYear: String? = null
)