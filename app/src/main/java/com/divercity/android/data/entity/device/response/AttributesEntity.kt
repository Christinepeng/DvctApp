package com.divercity.android.data.entity.device.response

import com.google.gson.annotations.SerializedName

data class AttributesEntity(

	@field:SerializedName("pn_count")
	val pnCount: Int? = null,

	@field:SerializedName("enabled")
	val enabled: Boolean? = null,

	@field:SerializedName("platform")
	val platform: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)