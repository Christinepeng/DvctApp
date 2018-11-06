package com.divercity.app.data.entity.location

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("country_name")
	val countryName: String? = null
)