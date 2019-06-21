package com.divercity.android.data.entity.company.companyadmin.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

data class UserEntity(

	@field:SerializedName("connected")
	val connected: String? = null,

	@field:SerializedName("avatar_medium")
	val avatarMedium: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("avatar_thumb")
	val avatarThumb: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("present")
	val present: Boolean? = null
)