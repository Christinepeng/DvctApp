package com.divercity.android.data.entity.user.discard

import com.google.gson.annotations.SerializedName

data class DiscardConnectionBody(

	@field:SerializedName("users")
	val users: List<String?>? = null
)