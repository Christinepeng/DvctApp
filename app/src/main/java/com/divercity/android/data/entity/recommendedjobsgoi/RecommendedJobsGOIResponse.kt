package com.divercity.android.data.entity.recommendedjobsgoi

import com.google.gson.annotations.SerializedName

data class RecommendedJobsGOIResponse(

	@field:SerializedName("goi")
	val goi: Goi? = null,

	@field:SerializedName("jobs")
	val jobs: Jobs? = null
)