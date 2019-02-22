package com.divercity.android.data.entity.group.answer.response

import com.google.gson.annotations.SerializedName

data class Relationships(

    @field:SerializedName("replied_to")
	val repliedTo: RepliedTo? = null,

    @field:SerializedName("question")
	val question: Question? = null
)