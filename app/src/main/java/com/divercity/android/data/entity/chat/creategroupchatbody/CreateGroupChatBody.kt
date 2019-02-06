package com.divercity.android.data.entity.chat.creategroupchatbody

import com.google.gson.annotations.SerializedName

data class CreateGroupChatBody(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("type")
	val type: String = "group",

	@field:SerializedName("picture")
	val picture: String? = null
)