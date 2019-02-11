package com.divercity.android.data.entity.group.contactinvitation.body

import com.google.gson.annotations.SerializedName

data class GroupInvite(

	@field:SerializedName("invite_type")
	val inviteType: String = "sms_invite",

	@field:SerializedName("phone_numbers")
	val phoneNumbers: List<String?>? = null,

	@field:SerializedName("group_id")
	val groupId: String? = null
)