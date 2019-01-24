package com.divercity.android.data.entity.device.body

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Device(

	@field:SerializedName("platform")
	val platform: String? = null,

	@field:SerializedName("enabled")
	val enabled: Boolean? = null,

	@field:SerializedName("token")
	val token: String? = null
)