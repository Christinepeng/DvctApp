package com.divercity.android.data.entity.group.creategroup

import com.google.gson.annotations.SerializedName

data class GroupOfInterest(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("group_type")
	val groupType: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null
)