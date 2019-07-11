package com.divercity.android.data.entity.ethnicity

import com.google.gson.annotations.SerializedName

data class EthnicityEntityResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String? = null
)