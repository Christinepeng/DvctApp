package com.divercity.android.data.entity.workexperience.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Attributes(

    @field:SerializedName("world_city_info")
	val worldCityInfo: WorldCityInfo? = null,

    @field:SerializedName("job_start")
	val jobStart: String? = null,

    @field:SerializedName("is_present")
	val isPresent: Boolean? = null,

    @field:SerializedName("role")
	val role: String? = null,

    @field:SerializedName("job_employer_info")
	val jobEmployerInfo: JobEmployerInfo? = null,

    @field:SerializedName("experience_details")
	val experienceDetails: String? = null,

    @field:SerializedName("job_end")
	val jobEnd: String? = null,

    @field:SerializedName("world_country_info")
	val worldCountryInfo: WorldCountryInfo? = null,

    @field:SerializedName("location_words")
	val locationWords: String? = null
)