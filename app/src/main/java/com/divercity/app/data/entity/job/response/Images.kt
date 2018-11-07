package com.divercity.app.data.entity.job.response

import com.google.gson.annotations.SerializedName

data class Images(

	@field:SerializedName("thumb")
	val thumb: String? = null,

	@field:SerializedName("main")
	val main: String? = null
)