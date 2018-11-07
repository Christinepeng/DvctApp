package com.divercity.app.data.entity.job.response

import com.google.gson.annotations.SerializedName

data class Employer(

        @field:SerializedName("cover_photos")
	val coverPhotos: CoverPhotos? = null,

        @field:SerializedName("name")
	val name: String? = null,

        @field:SerializedName("id")
	val id: Int? = null,

        @field:SerializedName("photos")
	val photos: Photos? = null
)