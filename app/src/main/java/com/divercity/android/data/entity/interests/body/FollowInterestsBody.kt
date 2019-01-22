package com.divercity.android.data.entity.interests.body

import com.google.gson.annotations.SerializedName

data class FollowInterestsBody(

	@field:SerializedName("interest_ids")
	val interestIds: List<String?>? = null
)