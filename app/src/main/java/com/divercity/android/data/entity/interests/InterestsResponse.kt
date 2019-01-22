package com.divercity.android.data.entity.interests

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

data class InterestsResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	var isSelected : Boolean = false
)