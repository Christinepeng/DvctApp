package com.divercity.android.data.entity.document

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("document")
	val document: String? = null,

	@field:SerializedName("last_used")
	val lastUsed: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)