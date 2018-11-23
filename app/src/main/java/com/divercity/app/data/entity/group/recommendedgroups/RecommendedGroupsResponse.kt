package com.divercity.app.data.entity.group.recommendedgroups

import com.google.gson.annotations.SerializedName

data class RecommendedGroupsResponse(

	@field:SerializedName("recommendation")
	val recommendation: List<RecommendationItem>? = null
)