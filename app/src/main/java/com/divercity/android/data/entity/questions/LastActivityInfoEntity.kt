package com.divercity.android.data.entity.questions

import com.google.gson.annotations.SerializedName

data class LastActivityInfoEntity(

	@field:SerializedName("answer_info")
	val answerInfo: AnswerInfoEntity? = null,

	@field:SerializedName("author_info")
	val authorInfo: AuthorInfoEntity? = null
)