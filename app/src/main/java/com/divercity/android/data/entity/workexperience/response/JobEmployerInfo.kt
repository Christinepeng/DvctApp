package com.divercity.android.data.entity.workexperience.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

data class JobEmployerInfo(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("picture_thumb")
	val pictureThumb: String? = null,

	@field:SerializedName("picture_main")
	val pictureMain: String? = null
)