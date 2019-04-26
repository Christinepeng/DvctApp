package com.divercity.android.data.entity.user.response

import com.google.gson.annotations.SerializedName

data class CompanyEntity(

	@field:SerializedName("cover_photos")
	val coverPhotos: CoverPhotosEntity? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("photos")
	val photos: PhotosEntity? = null
) {
	companion object{

		fun empty() = CompanyEntity()
	}
}