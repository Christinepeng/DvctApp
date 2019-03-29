package com.divercity.android.data.entity.group.invitation.user

import com.google.gson.annotations.SerializedName

data class GroupInviteUser(

	@field:SerializedName("invite_type")
	val inviteType: String = "in_app_invite",

	@field:SerializedName("group_id")
	val groupId: String? = null,

	@field:SerializedName("users")
	val users: List<String?>? = null
)