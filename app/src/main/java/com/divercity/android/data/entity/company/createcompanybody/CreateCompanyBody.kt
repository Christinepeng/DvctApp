package com.divercity.android.data.entity.company.createcompanybody

import com.google.gson.annotations.SerializedName

data class CreateCompanyBody(

	@field:SerializedName("companyName")
	val company: Company? = null
)