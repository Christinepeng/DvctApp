package com.divercity.android.data.entity.chat.currentchats

import com.google.gson.annotations.SerializedName

data class CurrentChatsResponse(

	@field:SerializedName("existing_users_chat_list")
	val existingUsersChatList: List<ExistingUsersChatListItem>? = null
)