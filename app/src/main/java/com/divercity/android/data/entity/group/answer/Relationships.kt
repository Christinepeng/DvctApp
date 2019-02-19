package com.divercity.android.data.entity.group.answer

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Relationships(

	@field:SerializedName("replied_to")
	val repliedTo: RepliedTo? = null,

	@field:SerializedName("question")
	val question: Question? = null
)