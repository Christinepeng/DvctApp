package com.divercity.android.data.entity.company.companyadmin.response

import com.google.gson.annotations.SerializedName

data class CompanyAdminResponse(

    @field:SerializedName("attributes")
	val attributes: Attributes? = null,

    @field:SerializedName("id")
	val id: String,

    @field:SerializedName("type")
	val type: String? = null
)