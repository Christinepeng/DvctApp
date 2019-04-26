package com.divercity.android.data.entity.company.companyadmin.response

import com.google.gson.annotations.SerializedName

data class CompanyAdminEntityResponse(

    @field:SerializedName("attributes")
	val attributes: AttributesEntity? = null,

    @field:SerializedName("id")
	val id: String,

    @field:SerializedName("type")
	val type: String? = null
)