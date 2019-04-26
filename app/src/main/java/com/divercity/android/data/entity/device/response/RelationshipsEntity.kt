package com.divercity.android.data.entity.device.response

import com.google.gson.annotations.SerializedName

data class RelationshipsEntity(

	@field:SerializedName("user")
	val user: UserEntity? = null
)