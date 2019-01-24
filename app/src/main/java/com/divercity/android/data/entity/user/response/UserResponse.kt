package com.divercity.android.data.entity.user.response

import com.divercity.android.features.chat.chat.model.ChatMember
import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("attributes")
	val userAttributes: UserAttributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
) {
	fun toChatMember() = ChatMember(id,
		userAttributes?.name,
		userAttributes?.avatarMedium,
		userAttributes?.accountType,
		0,0)
}