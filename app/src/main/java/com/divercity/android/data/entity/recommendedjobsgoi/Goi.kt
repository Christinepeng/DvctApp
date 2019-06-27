package com.divercity.android.data.entity.recommendedjobsgoi

import com.divercity.android.data.entity.group.group.GroupResponse
import com.google.gson.annotations.SerializedName

data class Goi(

	@field:SerializedName("data")
	val data: List<GroupResponse>? = null
)