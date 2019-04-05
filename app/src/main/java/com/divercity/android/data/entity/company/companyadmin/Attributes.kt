package com.divercity.android.data.entity.company.companyadmin

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Attributes(

	@field:SerializedName("employer")
	val employer: Employer? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)