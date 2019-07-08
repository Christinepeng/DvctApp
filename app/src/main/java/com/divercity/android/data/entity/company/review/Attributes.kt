package com.divercity.android.data.entity.company.review

import com.google.gson.annotations.SerializedName
import java.util.*

data class Attributes(

	@field:SerializedName("race_ethnicity_rate")
	val raceEthnicityRate: Int? = null,

	@field:SerializedName("sexual_orientation_rate")
	val sexualOrientationRate: Int? = null,

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("gender_rate")
	val genderRate: Int? = null,

	@field:SerializedName("able_bodiedness_rate")
	val ableBodiednessRate: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Date? = null,

	@field:SerializedName("age_rate")
	val ageRate: Int? = null
)