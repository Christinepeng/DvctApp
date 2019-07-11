package com.divercity.android.data.entity.photo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class ImageUrlsBySize(

	@field:SerializedName("thumb")
	val thumb: String? = null,

	@field:SerializedName("raw")
	val raw: String? = null,

	@field:SerializedName("regular")
	val regular: String? = null,

	@field:SerializedName("full")
	val full: String? = null
)