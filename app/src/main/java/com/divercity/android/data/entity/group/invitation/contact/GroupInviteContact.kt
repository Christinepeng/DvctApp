package com.divercity.android.data.entity.group.invitation.contact

import com.google.gson.annotations.SerializedName

data class GroupInviteContact(

	@field:SerializedName("invite_type")
	val inviteType: String = "sms_invite",

	@field:SerializedName("phone_numbers")
	val phoneNumbers: List<String?>? = null,

	@field:SerializedName("group_id")
	val groupId: String? = null
)