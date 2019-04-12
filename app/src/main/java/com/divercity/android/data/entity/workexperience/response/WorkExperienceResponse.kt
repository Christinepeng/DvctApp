package com.divercity.android.data.entity.workexperience.response

import com.google.gson.annotations.SerializedName

data class WorkExperienceResponse(

    @field:SerializedName("attributes")
	val attributes: Attributes? = null,

    @field:SerializedName("id")
	val id: String? = null,

    @field:SerializedName("type")
	val type: String? = null
)