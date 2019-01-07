package com.divercity.app.data.entity.chat.messages

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

data class Meta(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null
)