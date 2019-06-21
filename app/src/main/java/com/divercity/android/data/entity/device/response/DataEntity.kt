package com.divercity.android.data.entity.device.response

import com.google.gson.annotations.SerializedName

data class DataEntity(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)