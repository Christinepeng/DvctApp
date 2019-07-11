package com.divercity.android.data.entity.ethnicity

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("people_count")
	val peopleCount: Int? = null
)