package com.divercity.app.data.entity.job.response

import com.google.gson.annotations.SerializedName

data class Photos(

	@field:SerializedName("small")
	val small: String? = null,

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("large")
	val large: String? = null,

	@field:SerializedName("thumb")
	val thumb: String? = null,

	@field:SerializedName("main")
	val main: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null
)