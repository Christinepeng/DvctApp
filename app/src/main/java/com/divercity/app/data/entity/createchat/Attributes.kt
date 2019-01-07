package com.divercity.app.data.entity.createchat

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("chat_type")
	val chatType: String? = null,

	@field:SerializedName("identifier")
	val identifier: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("picture_small")
	val pictureSmall: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null
)