package com.divercity.android.data.entity.password

import com.google.gson.annotations.SerializedName

data class PasswordEntity(

	@field:SerializedName("new_confirmation")
	val newPasswordConfirmation: String? = null,

	@field:SerializedName("new")
	val newPassword: String? = null,

	@field:SerializedName("old")
	val old: String? = null
)