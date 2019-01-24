package com.divercity.android.data.entity.device.response

import com.google.gson.annotations.SerializedName

data class DeviceResponse(

	@field:SerializedName("relationships")
	val relationships: Relationships? = null,

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)