package com.divercity.android.data.entity.questions

import com.google.gson.annotations.SerializedName

data class AuthorInfoEntity(

	@field:SerializedName("avatar_medium")
	val avatarMedium: String? = null,

	@field:SerializedName("avatar_thumb")
	val avatarThumb: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("nickname")
	val nickname: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("present")
	val present: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null
)