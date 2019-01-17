package com.divercity.app.data.entity.createchat

import com.google.gson.annotations.SerializedName

data class CreateChatResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String? = null
)