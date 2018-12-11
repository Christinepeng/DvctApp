package com.divercity.app.data.entity.occupationofinterests

import com.google.gson.annotations.SerializedName

data class OccupationOfInterestResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)