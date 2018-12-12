package com.divercity.app.data.entity.interests

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Attributes(

	@field:SerializedName("showable")
	val showable: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("is_followed_by_current")
	val isFollowedByCurrent: Boolean? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("picture_main")
	val pictureMain: String? = null
)