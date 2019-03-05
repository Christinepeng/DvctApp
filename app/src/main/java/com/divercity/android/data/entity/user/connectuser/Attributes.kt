package com.divercity.android.data.entity.user.connectuser

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Attributes(

	@field:SerializedName("follower_id")
	val followerId: Int? = null,

	@field:SerializedName("followed_id")
	val followedId: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)