package com.divercity.app.data.entity.group.creategroup

import com.google.gson.annotations.SerializedName

data class CreateGroupBody(

	@field:SerializedName("group_of_interest")
	val groupOfInterest: GroupOfInterest? = null
)