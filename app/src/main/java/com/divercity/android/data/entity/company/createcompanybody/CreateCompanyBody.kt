package com.divercity.android.data.entity.company.createcompanybody

import com.google.gson.annotations.SerializedName

data class CreateCompanyBody(

	@field:SerializedName("company")
	val company: Company? = null
)