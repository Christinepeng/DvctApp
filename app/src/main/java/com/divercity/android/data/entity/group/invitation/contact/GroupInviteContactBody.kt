package com.divercity.android.data.entity.group.invitation.contact

import com.google.gson.annotations.SerializedName

data class GroupInviteContactBody(

	@field:SerializedName("group_invite")
	val groupInvite: GroupInviteContact? = null
)