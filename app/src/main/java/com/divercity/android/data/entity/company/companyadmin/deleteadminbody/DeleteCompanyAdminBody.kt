package com.divercity.android.data.entity.company.companyadmin.deleteadminbody

import com.google.gson.annotations.SerializedName

data class DeleteCompanyAdminBody(

	@field:SerializedName("user_id")
	val userId: List<String?>? = null
)