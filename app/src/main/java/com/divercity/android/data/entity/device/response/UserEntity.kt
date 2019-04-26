package com.divercity.android.data.entity.device.response

import com.google.gson.annotations.SerializedName

data class UserEntity(

	@field:SerializedName("data")
	val data: DataEntity? = null
)