package com.divercity.android.data.entity.company.companyadmin.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

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