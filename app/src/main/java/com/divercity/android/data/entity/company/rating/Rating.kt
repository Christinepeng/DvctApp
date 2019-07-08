package com.divercity.android.data.entity.company.rating

import com.google.gson.annotations.SerializedName

data class Rating(

	@field:SerializedName("race_ethnicity_rate")
	val raceEthnicityRate: Int? = null,

	@field:SerializedName("sexual_orientation_rate")
	val sexualOrientationRate: Int? = null,

	@field:SerializedName("gender_rate")
	val genderRate: Int? = null,

	@field:SerializedName("able_bodiedness_rate")
	val ableBodiednessRate: Int? = null,

	@field:SerializedName("age_rate")
	val ageRate: Int? = null,

	@field:SerializedName("review")
	val review: String? = null
)