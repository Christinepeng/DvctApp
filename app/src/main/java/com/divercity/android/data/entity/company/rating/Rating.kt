package com.divercity.android.data.entity.company.rating

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Rating(

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("review")
	val review: String? = null
)