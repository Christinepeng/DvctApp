package com.divercity.app.data.entity.industry.body

import com.google.gson.annotations.SerializedName

data class FollowIndustryBody(

	@field:SerializedName("industry_ids")
	val industryIds: List<String?>? = null
)