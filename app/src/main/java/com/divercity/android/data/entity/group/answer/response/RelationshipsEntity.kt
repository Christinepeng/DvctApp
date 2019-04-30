package com.divercity.android.data.entity.group.answer.response

import com.google.gson.annotations.SerializedName

data class RelationshipsEntity(

    @field:SerializedName("replied_to")
	val repliedTo: RepliedToEntity? = null,

    @field:SerializedName("question")
	val question: QuestionEntity? = null
)