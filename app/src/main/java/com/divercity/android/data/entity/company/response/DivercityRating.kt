package com.divercity.android.data.entity.company.response

import com.google.gson.annotations.SerializedName

data class DivercityRating(

	@field:SerializedName("race_ethnicity_rate")
	val raceEthnicityRate: Int? = null,

	@field:SerializedName("total_divercity_rating")
	val totalDivercityRating: Int? = null,

	@field:SerializedName("sexual_orientation_rate")
	val sexualOrientationRate: Int? = null,

	@field:SerializedName("aggregate_rating")
	val aggregateRating: Int? = null,

	@field:SerializedName("gender_rate")
	val genderRate: Int? = null,

	@field:SerializedName("able_bodiedness_rate")
	val ableBodiednessRate: Int? = null,

	@field:SerializedName("age_rate")
	val ageRate: Int? = null
)