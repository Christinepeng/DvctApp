package com.divercity.android.data.entity.group.groupadmin

import com.google.gson.annotations.SerializedName

data class AddGroupAdminsBody(

	@field:SerializedName("group_of_interest_id")
	val groupOfInterestId: String? = null,

	@field:SerializedName("permission_type")
	val permissionType: String = "regular_admin",

	@field:SerializedName("user_id")
	val userId: String? = null
)