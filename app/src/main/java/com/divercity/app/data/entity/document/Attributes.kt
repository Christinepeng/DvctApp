package com.divercity.app.data.entity.document

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("document")
	val document: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)