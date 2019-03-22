package com.divercity.android.data.entity.chat.messages

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Meta(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null
)