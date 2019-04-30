package com.divercity.android.data.entity.questions

import com.google.gson.annotations.SerializedName

data class GroupItemEntity(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)