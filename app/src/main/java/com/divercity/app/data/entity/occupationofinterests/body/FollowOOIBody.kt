package com.divercity.app.data.entity.occupationofinterests.body

import com.google.gson.annotations.SerializedName

data class FollowOOIBody(

	@field:SerializedName("occupation_ids")
	val occupationIds: List<String?>? = null
)