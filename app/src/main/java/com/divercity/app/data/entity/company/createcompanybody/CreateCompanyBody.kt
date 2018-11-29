package com.divercity.app.data.entity.company.createcompanybody

import com.google.gson.annotations.SerializedName

data class CreateCompanyBody(

	@field:SerializedName("company")
	val company: Company? = null
)