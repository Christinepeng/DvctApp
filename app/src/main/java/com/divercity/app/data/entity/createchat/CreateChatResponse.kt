package com.divercity.app.data.entity.createchat

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

data class CreateChatResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)