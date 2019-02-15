package com.divercity.android.data.entity.group.requests

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class UserInfo(

	@field:SerializedName("avatar_medium")
	val avatarMedium: String? = null,

	@field:SerializedName("avatar_thumb")
	val avatarThumb: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("present")
	val present: Boolean? = null
)