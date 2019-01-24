package com.divercity.android.data.entity.device.response

import com.google.gson.annotations.SerializedName

data class Relationships(

	@field:SerializedName("user")
	val user: User? = null
)