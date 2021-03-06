package com.divercity.app.data.entity.jobapplication


import com.google.gson.annotations.SerializedName


data class Photos(

	@field:SerializedName("thumb")
	val thumb: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null,

	@field:SerializedName("original")
	val original: String? = null
)