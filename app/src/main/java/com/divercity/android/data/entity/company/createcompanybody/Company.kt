package com.divercity.android.data.entity.company.createcompanybody

import com.google.gson.annotations.SerializedName

data class Company(

	@field:SerializedName("headquarters")
	val headquarters: String? = null,

	@field:SerializedName("industry_id")
	val industryId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("user_company_size_id")
	val userCompanySizeId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null
)