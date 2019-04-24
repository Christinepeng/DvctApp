package com.divercity.android.data.entity.company.review

import com.google.gson.annotations.SerializedName
import java.util.*

data class Attributes(

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Date? = null
)