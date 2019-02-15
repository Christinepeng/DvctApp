package com.divercity.android.data.entity.group.requests

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class GroupInfo(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("group_type")
	val groupType: String? = null,

	@field:SerializedName("present")
	val present: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("picture_main")
	val pictureMain: String? = null
)