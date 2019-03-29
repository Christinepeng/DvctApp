package com.divercity.android.data.entity.group.invitationnotification

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("invitee_info")
	val inviteeInfo: InviteeInfo? = null,

	@field:SerializedName("group_info")
	val groupInfo: GroupInfo? = null,

	@field:SerializedName("read")
	val read: Boolean? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("body")
	var body: String? = null,

	@field:SerializedName("status")
	var status: String? = null
)