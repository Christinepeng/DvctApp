package com.divercity.app.data.entity.job.response

import com.google.gson.annotations.SerializedName

data class JobTypeInfo(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)