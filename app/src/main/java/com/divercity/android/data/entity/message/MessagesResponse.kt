package com.divercity.android.data.entity.message

import com.google.gson.annotations.SerializedName

data class MessagesResponse(

	@field:SerializedName("hasErrors")
	val hasErrors: Boolean? = null,

	@field:SerializedName("messages")
	val messages: List<String?>? = null
)