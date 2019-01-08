package com.divercity.app.data.entity.user.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)