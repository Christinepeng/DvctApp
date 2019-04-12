package com.divercity.android.data.entity.workexperience.body

import com.google.gson.annotations.SerializedName

data class Experience(

	@field:SerializedName("job_start")
	val jobStart: String? = null,

	@field:SerializedName("is_present")
	val isPresent: Boolean? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("job_employer_id")
	val jobEmployerId: String? = null,

	@field:SerializedName("experience_details")
	val experienceDetails: String? = null,

	@field:SerializedName("world_country_id")
	val worldCountryId: Int? = null,

	@field:SerializedName("job_end")
	val jobEnd: String? = null,

	@field:SerializedName("location_words")
	val locationWords: String? = null,

	@field:SerializedName("world_city_id")
	val worldCityId: Int? = null
)