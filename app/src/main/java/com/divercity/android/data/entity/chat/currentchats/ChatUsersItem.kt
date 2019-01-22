package com.divercity.android.data.entity.chat.currentchats

import com.google.gson.annotations.SerializedName

data class ChatUsersItem(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("id")
	var id: Int? = null
) {
	constructor() : this("", 0)
}