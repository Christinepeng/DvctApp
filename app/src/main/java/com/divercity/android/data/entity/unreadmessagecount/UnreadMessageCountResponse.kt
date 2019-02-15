package com.divercity.android.data.entity.unreadmessagecount

import com.google.gson.annotations.SerializedName

data class UnreadMessageCountResponse(

	@field:SerializedName("count")
	val count: Int? = null
)