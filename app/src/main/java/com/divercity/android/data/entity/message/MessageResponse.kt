package com.divercity.android.data.entity.message

import com.google.gson.annotations.SerializedName

data class MessageResponse(

	@field:SerializedName("message")
	val message: String? = null
)