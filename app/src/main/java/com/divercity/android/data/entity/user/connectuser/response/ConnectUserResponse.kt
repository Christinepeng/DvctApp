package com.divercity.android.data.entity.user.connectuser.response

import com.google.gson.annotations.SerializedName

data class ConnectUserResponse(

    @field:SerializedName("attributes")
	val attributes: Attributes? = null,

    @field:SerializedName("id")
	val id: String? = null,

    @field:SerializedName("type")
	val type: String? = null
)