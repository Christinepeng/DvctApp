package com.divercity.android.data.entity.device.response

import com.google.gson.annotations.SerializedName

data class DeviceEntityResponse(

	@field:SerializedName("relationships")
	val relationships: RelationshipsEntity? = null,

	@field:SerializedName("attributes")
	val attributes: AttributesEntity? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)