package com.divercity.android.data.entity.activity

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("read")
	val read: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("updated_ago_in_words")
	val updatedAgoInWords: String? = null,

	@field:SerializedName("last_active_user_info")
	val lastActiveUserInfo: LastActiveUserInfo? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("a_type")
	val aType: String? = null
)