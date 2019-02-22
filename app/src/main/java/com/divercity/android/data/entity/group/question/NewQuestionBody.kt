package com.divercity.android.data.entity.group.question

import com.google.gson.annotations.SerializedName

data class NewQuestionBody(

	@field:SerializedName("question")
	val question: Question? = null
)