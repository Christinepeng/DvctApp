package com.divercity.android.data.entity.degree

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("people_count")
	val peopleCount: Int? = null
)