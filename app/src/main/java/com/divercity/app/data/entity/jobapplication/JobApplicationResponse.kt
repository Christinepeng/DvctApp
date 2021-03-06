package com.divercity.app.data.entity.jobapplication


import com.google.gson.annotations.SerializedName


data class JobApplicationResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)