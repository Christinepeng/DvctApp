package com.divercity.android.data.entity.chat.addchatmemberbody

import com.google.gson.annotations.SerializedName

data class AddChatMemberBody(

	@field:SerializedName("add")
	val add: List<String>? = null
)