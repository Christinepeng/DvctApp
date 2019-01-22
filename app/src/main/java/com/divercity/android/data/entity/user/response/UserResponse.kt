package com.divercity.android.data.entity.user.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("attributes")
	val userAttributes: UserAttributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)