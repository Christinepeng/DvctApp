package com.divercity.android.data.entity.company.review

import com.google.gson.annotations.SerializedName

data class CompanyDiversityReviewEntityResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String? = null
)