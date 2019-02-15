package com.divercity.android.data.entity.jobapplication

import com.google.gson.annotations.SerializedName

data class Employer(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("cover_photos")
	val coverPhotos: CoverPhotos? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("photos")
	val photos: Photos? = null
)