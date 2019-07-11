package com.divercity.android.data.entity.photo

import com.google.gson.annotations.SerializedName

data class PhotoEntityResponse(

	@field:SerializedName("image_urls_by_size")
	val imageUrlsBySize: ImageUrlsBySize? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("image_id")
	val imageId: String? = null
)