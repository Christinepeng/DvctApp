package com.divercity.android.data.entity.workexperience.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

data class WorldCityInfo(

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("country_name")
	val countryName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)