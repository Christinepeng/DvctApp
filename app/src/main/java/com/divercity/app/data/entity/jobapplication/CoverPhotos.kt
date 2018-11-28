package com.divercity.app.data.entity.jobapplication


import com.google.gson.annotations.SerializedName


data class CoverPhotos(

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("thumb")
	val thumb: String? = null
)