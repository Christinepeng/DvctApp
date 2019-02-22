package com.divercity.android.data.entity.group.answer.body

import com.google.gson.annotations.SerializedName

data class Answer(

	@field:SerializedName("images[]")
	val images: List<String?>? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("question_id")
	val questionId: String? = null
)