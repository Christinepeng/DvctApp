package com.divercity.android.data.entity.user.followuser

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("follower_id")
	val followerId: Int? = null,

	@field:SerializedName("followed_id")
	val followedId: Int? = null
)