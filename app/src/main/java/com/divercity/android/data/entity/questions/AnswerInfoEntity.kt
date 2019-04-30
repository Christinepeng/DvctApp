package com.divercity.android.data.entity.questions

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class AnswerInfoEntity(

	@field:SerializedName("images")
	val images: List<Any?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("text")
	val text: String? = null
)