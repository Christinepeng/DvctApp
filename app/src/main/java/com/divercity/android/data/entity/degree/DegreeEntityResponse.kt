package com.divercity.android.data.entity.degree

import com.google.gson.annotations.SerializedName

data class DegreeEntityResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes,

	@field:SerializedName("id")
	val id: String
)