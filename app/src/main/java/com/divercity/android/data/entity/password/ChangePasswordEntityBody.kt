package com.divercity.android.data.entity.password

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

data class ChangePasswordEntityBody(

	@field:SerializedName("password")
	val password: PasswordEntity? = null
)