package com.divercity.android.data.entity.group.requests

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("group_info")
	val groupInfo: GroupInfo? = null,

	@field:SerializedName("read")
	val read: Boolean? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("user_info")
	val userInfo: UserInfo? = null,

	@field:SerializedName("acceptor_info")
	val acceptorInfo: AcceptorInfo? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("state")
	var state: String? = null
)