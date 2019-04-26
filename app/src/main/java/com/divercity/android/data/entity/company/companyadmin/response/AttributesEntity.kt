package com.divercity.android.data.entity.company.companyadmin.response

import com.google.gson.annotations.SerializedName

data class AttributesEntity(

    @field:SerializedName("employer")
	val employer: EmployerEntity? = null,

    @field:SerializedName("user")
	val user: UserEntity? = null,

    @field:SerializedName("email")
	val email: String? = null,

    @field:SerializedName("status")
	val status: String? = null
)