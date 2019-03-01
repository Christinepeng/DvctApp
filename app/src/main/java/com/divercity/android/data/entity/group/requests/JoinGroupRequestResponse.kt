package com.divercity.android.data.entity.group.requests

import com.divercity.android.data.entity.group.ConnectionItem
import com.google.gson.annotations.SerializedName

data class JoinGroupRequestResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
) : ConnectionItem