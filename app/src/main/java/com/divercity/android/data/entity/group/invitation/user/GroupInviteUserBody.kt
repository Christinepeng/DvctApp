package com.divercity.android.data.entity.group.invitation.user

import com.google.gson.annotations.SerializedName

data class GroupInviteUserBody(

	@field:SerializedName("group_invite")
	val groupInvite: GroupInviteUser? = null
)