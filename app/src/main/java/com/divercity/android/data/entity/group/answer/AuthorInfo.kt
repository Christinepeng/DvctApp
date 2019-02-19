package com.divercity.android.data.entity.group.answer

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class AuthorInfo(

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
	val present: Boolean? = null
)